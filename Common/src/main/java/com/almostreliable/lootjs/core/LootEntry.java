package com.almostreliable.lootjs.core;

import com.almostreliable.lootjs.kube.LootConditionsContainer;
import com.almostreliable.lootjs.loot.LootFunctionsContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LootEntry implements LootFunctionsContainer<LootEntry> {

    private final ItemStack itemStack;
    private final List<LootItemFunction> postModifications = new ArrayList<>();
    private final List<ILootCondition> conditions = new ArrayList<>();
    private int weight = 1;

    public LootEntry(Item item) {
        this.itemStack = new ItemStack(item);
    }

    public LootEntry(ItemStack itemStack) {
        this.itemStack = itemStack;
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
}
