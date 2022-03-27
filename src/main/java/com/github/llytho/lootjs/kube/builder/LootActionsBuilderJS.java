package com.github.llytho.lootjs.kube.builder;

import com.github.llytho.lootjs.core.ILootAction;
import com.github.llytho.lootjs.core.ILootCondition;
import com.github.llytho.lootjs.core.ILootHandler;
import com.github.llytho.lootjs.kube.ConditionsContainer;
import com.github.llytho.lootjs.kube.LootContextJS;
import com.github.llytho.lootjs.kube.action.CustomJSAction;
import com.github.llytho.lootjs.loot.LootActionsContainer;
import com.github.llytho.lootjs.loot.LootFunctionsContainer;
import com.github.llytho.lootjs.loot.LootPoolBuilder;
import com.github.llytho.lootjs.loot.action.*;
import com.github.llytho.lootjs.util.Utils;
import com.github.llytho.lootjs.util.WeightedItemStack;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unused")
public class LootActionsBuilderJS implements ConditionsContainer<LootActionsBuilderJS>,
                                             LootFunctionsContainer<LootActionsBuilderJS>,
                                             LootActionsContainer<LootActionsBuilderJS> {
    public static final String DEPRECATED_MSG = "1.18.2-2.3.0 Will be removed in future versions. Please use ";

    private final List<ILootHandler> handlers = new ArrayList<>();
    private String logName;

    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "name(name)")
    public LootActionsBuilderJS logName(String name) {
        ConsoleJS.SERVER.warn(DEPRECATED_MSG + "name(name)");
        logName = name;
        return this;
    }

    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "apply((ctx) => {})")
    public LootActionsBuilderJS thenApply(Consumer<LootContextJS> action) {
        ConsoleJS.SERVER.warn(DEPRECATED_MSG + "apply((ctx) => {})");
        handlers.add(new CustomJSAction(action));
        return this;
    }

    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "addLoot([items])")
    public LootActionsBuilderJS thenAdd(ItemStackJS... itemStacks) {
        ConsoleJS.SERVER.warn(DEPRECATED_MSG + "addLoot([items])");
        ItemStack[] vanillaItemStacks = Arrays
                .stream(itemStacks)
                .filter(is -> !is.isEmpty())
                .map(ItemStackJS::getItemStack)
                .toArray(ItemStack[]::new);
        handlers.add(new AddLootAction(vanillaItemStacks));
        return this;
    }


    @Deprecated(forRemoval = true, since = DEPRECATED_MSG +
                                           "addWeightedLoot(numberProvider, allowDuplicateLoot, [items])")
    public LootActionsBuilderJS thenAddWeighted(MinMaxBounds.Ints interval, boolean allowDuplicateLoot, WeightedItemStack[] itemStacks) {
        ConsoleJS.SERVER.warn(DEPRECATED_MSG + "addWeightedLoot(interval, allowDuplicateLoot, [items])");
        var weightedListBuilder = SimpleWeightedRandomList.<ItemStack>builder();
        for (var wis : itemStacks) {
            weightedListBuilder.add(wis.getItemStack(), wis.getWeight());
        }

        NumberProvider provider;
        if (interval.getMin() != null && interval.getMax() != null) {
            provider = UniformGenerator.between(interval.getMin(), interval.getMax());
        } else if (interval.getMin() != null) {
            provider = ConstantValue.exactly(interval.getMin());
        } else if (interval.getMax() != null) {
            provider = ConstantValue.exactly(interval.getMax());
        } else {
            provider = ConstantValue.exactly(1);
        }

        handlers.add(new WeightedAddLootAction(provider, weightedListBuilder.build(), allowDuplicateLoot));
        return this;
    }

    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "addWeightedLoot(interval, [items])")
    public LootActionsBuilderJS thenAddWeighted(MinMaxBounds.Ints interval, WeightedItemStack[] itemStacks) {
        ConsoleJS.SERVER.warn(DEPRECATED_MSG + "addWeightedLoot(interval, [items])");
        return thenAddWeighted(interval, true, itemStacks);
    }

    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "addWeightedLoot([items])")
    public LootActionsBuilderJS thenAddWeighted(WeightedItemStack[] itemStacks) {
        ConsoleJS.SERVER.warn(DEPRECATED_MSG + "addWeightedLoot([items])");
        return thenAddWeighted(MinMaxBounds.Ints.atMost(1), true, itemStacks);
    }


    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "removeLoot(ingredient)")
    public LootActionsBuilderJS thenRemove(IngredientJS ingredient) {
        ConsoleJS.SERVER.warn(DEPRECATED_MSG + "removeLoot(ingredient)");
        handlers.add(new RemoveLootAction(ingredient.getVanillaPredicate()));
        return this;
    }


    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "replaceLoot(ingredient, item)")
    public LootActionsBuilderJS thenReplace(IngredientJS ingredient, ItemStackJS itemStack) {
        ConsoleJS.SERVER.warn(DEPRECATED_MSG + "replaceLoot(ingredient, item)");
        handlers.add(new ReplaceLootAction(ingredient.getVanillaPredicate(), itemStack.getItemStack()));
        return this;
    }

    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "modifyLoot(ingredient, (item) => { return item; })")
    public LootActionsBuilderJS thenModify(IngredientJS ingredient, Function<ItemStackJS, ItemStackJS> function) {
        ConsoleJS.SERVER.warn(DEPRECATED_MSG + "modifyLoot(ingredient, (item) => { return item; })");
        handlers.add(new ModifyLootAction(ingredient.getVanillaPredicate(),
                (itemStack) -> function.apply(new ItemStackJS(itemStack)).getItemStack()));
        return this;
    }

    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "triggerExplosion(radius, destroy, fire)")
    public LootActionsBuilderJS thenExplode(float radius, boolean destroy, boolean fire) {
        ConsoleJS.SERVER.warn(DEPRECATED_MSG + "triggerExplosion(radius, destroy, fire)");
        Explosion.BlockInteraction mode =
                destroy ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
        handlers.add(new ExplodeAction(radius, mode, fire));
        return this;
    }

    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "triggerLightningStrike(shouldDamage)")
    public LootActionsBuilderJS thenLightningStrike(boolean shouldDamage) {
        ConsoleJS.SERVER.warn(DEPRECATED_MSG + "triggerLightningStrike(shouldDamage)");
        handlers.add(new LightningStrikeAction(shouldDamage));
        return this;
    }

    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "pool((pool) => {})")
    public LootActionsBuilderJS thenRollPool(Consumer<LootActionsBuilderJS> callback) {
        ConsoleJS.SERVER.warn(DEPRECATED_MSG + "pool((pool) => {})");
        return thenRollPool(MinMaxBounds.Ints.exactly(1), callback);
    }

    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "pool(interval, (pool) => {})")
    public LootActionsBuilderJS thenRollPool(MinMaxBounds.Ints interval, Consumer<LootActionsBuilderJS> callback) {
        ConsoleJS.SERVER.warn(DEPRECATED_MSG + "pool(interval, (pool) => {})");
        LootActionsBuilderJS poolBuilder = new LootActionsBuilderJS();
        callback.accept(poolBuilder);
        List<ILootHandler> poolHandlers = poolBuilder.getHandlers();

        NumberProvider provider;
        if (interval.getMin() != null && interval.getMax() != null) {
            provider = UniformGenerator.between(interval.getMin(), interval.getMax());
        } else if (interval.getMin() != null) {
            provider = ConstantValue.exactly(interval.getMin());
        } else if (interval.getMax() != null) {
            provider = ConstantValue.exactly(interval.getMax());
        } else {
            provider = ConstantValue.exactly(1);
        }

        handlers.add(new LootPoolAction(provider, poolHandlers));
        return this;
    }

    @HideFromJS
    public List<ILootHandler> getHandlers() {
        if (handlers.isEmpty()) {
            throw new IllegalArgumentException("No actions were added to the modifier");
        }

        return handlers;
    }

    public String getLogName(String alternative) {
        if (logName == null) {
            return alternative;
        }

        return Utils.quote(logName);
    }

    @Override
    public LootActionsBuilderJS addCondition(ILootCondition condition) {
        handlers.add(condition);
        return this;
    }

    @Override
    public LootActionsBuilderJS addAction(ILootAction action) {
        handlers.add(action);
        return this;
    }

    // TODO refactor them
    public LootActionsBuilderJS pool(Consumer<LootPoolBuilder> callback) {
        LootPoolBuilder poolBuilder = new LootPoolBuilder();
        callback.accept(poolBuilder);
        handlers.add(poolBuilder.build());
        return this;
    }

    public LootActionsBuilderJS apply(Consumer<LootContextJS> action) {
        handlers.add(new CustomJSAction(action));
        return this;
    }
}
