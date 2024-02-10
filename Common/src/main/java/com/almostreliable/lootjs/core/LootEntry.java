package com.almostreliable.lootjs.core;

import com.almostreliable.lootjs.kube.LootConditionsContainer;
import com.almostreliable.lootjs.loot.LootFunctionsContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LootEntry implements LootFunctionsContainer<LootEntry> {

    private final Generator generator;
    private final List<LootItemFunction> postModifications = new ArrayList<>();
    private final List<ILootCondition> conditions = new ArrayList<>();
    private int weight = 1;

    public LootEntry(Item item) {
        this.generator = new ItemGenerator(new ItemStack(item));
    }

    public LootEntry(ItemStack itemStack) {
        this.generator = new ItemGenerator(itemStack);
    }

    public LootEntry(Generator generator) {
        this.generator = generator;
    }

    /**
     * Creates a new ItemStack from the itemStack and applies all post modifications to it. May return null if all conditions fail.
     *
     * @param context the context
     * @return the item stack or null, if all conditions fail
     */
    @Nullable
    public ItemStack createItem(LootContext context) {
        if (!matchesConditions(context)) {
            return null;
        }

        ItemStack itemStack = generator.create(context);
        if (itemStack == null) {
            return null;
        }

        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack result = itemStack.copy();
        for (LootItemFunction lootItemFunction : postModifications) {
            result = lootItemFunction.apply(result, context);
        }

        return result;
    }

    @Override
    public LootEntry addFunction(LootItemFunction lootItemFunction) {
        postModifications.add(lootItemFunction);
        return this;
    }

    public boolean matchesConditions(LootContext context) {
        for (ILootCondition condition : conditions) {
            if (!condition.test(context)) {
                return false;
            }
        }

        return true;
    }

    public boolean hasWeight() {
        return weight >= 1;
    }

    public int getWeight() {
        return weight;
    }

    public LootEntry withWeight(int weight) {
        this.weight = Math.max(1, weight);
        return this;
    }

    public LootEntry withChance(int chance) {
        return withWeight(chance);
    }

    public LootEntry when(Consumer<LootConditionsContainer<?>> action) {
        List<ILootCondition> conditions = new ArrayList<>();
        var lcc = new LootConditionsContainer<>() {
            @Override
            public LootConditionsContainer<?> addCondition(ILootCondition condition) {
                conditions.add(condition);
                return this;
            }
        };

        action.accept(lcc);
        this.conditions.addAll(conditions);
        return this;
    }

    public interface Generator {

        @Nullable
        ItemStack create(LootContext context);
    }

    public record ItemGenerator(ItemStack item) implements Generator {
        @Nullable
        @Override
        public ItemStack create(LootContext context) {
            return item;
        }
    }

    public record RandomIngredientGenerator(Ingredient ingredient) implements Generator {
        @Nullable
        @Override
        public ItemStack create(LootContext context) {
            ItemStack[] items = ingredient.getItems();
            if (items.length == 0) {
                return ItemStack.EMPTY;
            }

            int index = context.getRandom().nextInt(items.length);
            return items[index];
        }
    }

    public record VanillaWrappedLootEntry(LootPoolEntryContainer entry) implements Generator {
        @Override
        public ItemStack create(LootContext context) {
            List<ItemStack> items = new ArrayList<>();
            entry.expand(context, entry -> entry.createItemStack(items::add, context));

            if (items.isEmpty()) {
                return null;
            }

            return items.get(context.getRandom().nextInt(items.size()));
        }
    }
}
