package com.github.llytho.lootjs.kube.builder;

import com.github.llytho.lootjs.core.ILootCondition;
import com.github.llytho.lootjs.core.ILootHandler;
import com.github.llytho.lootjs.kube.ConditionsContainer;
import com.github.llytho.lootjs.kube.LootContextJS;
import com.github.llytho.lootjs.kube.action.CustomJSAction;
import com.github.llytho.lootjs.loot.action.*;
import com.github.llytho.lootjs.util.Utils;
import com.github.llytho.lootjs.util.WeightedItemStack;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unused")
public class LootActionsBuilderJS implements ConditionsContainer<LootActionsBuilderJS> {
    public static final String DEPRECATED_MSG = "1.18.2-2.3.0 Will be removed in future versions. Please use ";

    private final List<ILootHandler> handlers = new ArrayList<>();
    private String logName;

    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "name(name)")
    public LootActionsBuilderJS logName(String name) {
        logName = name;
        return this;
    }

    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "apply((ctx) => {})")
    public LootActionsBuilderJS thenApply(Consumer<LootContextJS> action) {
        handlers.add(new CustomJSAction(action));
        return this;
    }

    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "addLoot([items])")
    public LootActionsBuilderJS thenAdd(ItemStackJS... itemStacks) {
        ItemStack[] vanillaItemStacks = Arrays
                .stream(itemStacks)
                .filter(is -> !is.isEmpty())
                .map(ItemStackJS::getItemStack)
                .toArray(ItemStack[]::new);
        handlers.add(new AddLootAction(vanillaItemStacks));
        return this;
    }


    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "addWeightedLoot(interval, allowDuplicateLoot, [items])")
    public LootActionsBuilderJS thenAddWeighted(MinMaxBounds.Ints interval, boolean allowDuplicateLoot, WeightedItemStack[] itemStacks) {
        var weightedListBuilder = SimpleWeightedRandomList.<ItemStack>builder();
        for (var wis : itemStacks) {
            weightedListBuilder.add(wis.getItemStack(), wis.getWeight());
        }
        handlers.add(new WeightedAddLootAction(interval, weightedListBuilder.build(), allowDuplicateLoot));
        return this;
    }

    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "addWeightedLoot(interval, [items])")
    public LootActionsBuilderJS thenAddWeighted(MinMaxBounds.Ints interval, WeightedItemStack[] itemStacks) {
        return thenAddWeighted(interval, true, itemStacks);
    }

    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "addWeightedLoot([items])")
    public LootActionsBuilderJS thenAddWeighted(WeightedItemStack[] itemStacks) {
        return thenAddWeighted(MinMaxBounds.Ints.atMost(1), true, itemStacks);
    }


    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "removeLoot(ingredient)")
    public LootActionsBuilderJS thenRemove(IngredientJS ingredient) {
        handlers.add(new RemoveLootAction(ingredient.getVanillaPredicate()));
        return this;
    }


    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "replaceLoot(ingredient, item)")
    public LootActionsBuilderJS thenReplace(IngredientJS ingredient, ItemStackJS itemStack) {
        handlers.add(new ReplaceLootAction(ingredient.getVanillaPredicate(), itemStack.getItemStack()));
        return this;
    }

    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "modifyLoot(ingredient, (item) => { return item; })")
    public LootActionsBuilderJS thenModify(IngredientJS ingredient, Function<ItemStackJS, ItemStackJS> function) {
        handlers.add(new ModifyLootAction(ingredient.getVanillaPredicate(),
                (itemStack) -> function.apply(new ItemStackJS(itemStack)).getItemStack()));
        return this;
    }

    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "triggerExplosion(radius, destroy, fire)")
    public LootActionsBuilderJS thenExplode(float radius, boolean destroy, boolean fire) {
        Explosion.BlockInteraction mode =
                destroy ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
        handlers.add(new ExplodeAction(radius, mode, fire));
        return this;
    }

    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "triggerLightningStrike(shouldDamage)")
    public LootActionsBuilderJS thenLightningStrike(boolean shouldDamage) {
        handlers.add(new LightningStrikeAction(shouldDamage));
        return this;
    }

    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "pool((pool) => {})")
    public LootActionsBuilderJS addPool(Consumer<LootActionsBuilderJS> callback) {
        return thenRollPool(MinMaxBounds.Ints.exactly(1), callback);
    }

    @Deprecated(forRemoval = true, since = DEPRECATED_MSG + "pool(interval, (pool) => {})")
    public LootActionsBuilderJS thenRollPool(MinMaxBounds.Ints interval, Consumer<LootActionsBuilderJS> callback) {
        LootActionsBuilderJS poolBuilder = new LootActionsBuilderJS();
        callback.accept(poolBuilder);
        List<ILootHandler> poolHandlers = poolBuilder.getHandlers();
        handlers.add(new RollPoolAction(interval, poolHandlers));
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
}
