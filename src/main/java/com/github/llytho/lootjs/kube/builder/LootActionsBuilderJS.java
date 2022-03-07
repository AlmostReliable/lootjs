package com.github.llytho.lootjs.kube.builder;

import com.github.llytho.lootjs.core.ILootHandler;
import com.github.llytho.lootjs.kube.ConditionsContainer;
import com.github.llytho.lootjs.kube.LootContextJS;
import com.github.llytho.lootjs.kube.action.CustomJSAction;
import com.github.llytho.lootjs.loot.action.*;
import com.github.llytho.lootjs.util.Utils;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class LootActionsBuilderJS implements ConditionsContainer<LootActionsBuilderJS> {
    private final List<ILootHandler> handlers = new ArrayList<>();
    private String logName;

    public LootActionsBuilderJS logName(String name) {
        logName = name;
        return this;
    }

    public LootActionsBuilderJS thenApply(Consumer<LootContextJS> action) {
        handlers.add(new CustomJSAction(action));
        return this;
    }

    public LootActionsBuilderJS thenAdd(ItemStackJS... itemStacks) {
        ItemStack[] vanillaItemStacks = Arrays
                .stream(itemStacks)
                .filter(is -> !is.isEmpty())
                .map(ItemStackJS::getItemStack)
                .toArray(ItemStack[]::new);
        handlers.add(new AddLootAction(vanillaItemStacks));
        return this;
    }

    public LootActionsBuilderJS thenAddWeighted(MinMaxBounds.Ints interval, boolean allowDuplicateLoot, ItemStackJS[] itemStacks) {
        var weightedListBuilder = SimpleWeightedRandomList.<ItemStack>builder();
        for (ItemStackJS itemStack : itemStacks) {
            if (itemStack.isEmpty()) {
                continue;
            }

            int weight = itemStack.hasChance() ? (int) itemStack.getChance() : 1;
            if (weight == 0) {
                throw new IllegalArgumentException("Chance/Weight cannot be 0 for an item");
            }
            weightedListBuilder.add(itemStack.getItemStack(), weight);
        }
        handlers.add(new WeightedAddLootAction(interval, weightedListBuilder.build(), allowDuplicateLoot));
        return this;
    }

    public LootActionsBuilderJS thenAddWeighted(MinMaxBounds.Ints interval, ItemStackJS[] itemStacks) {
        return thenAddWeighted(interval, true, itemStacks);
    }

    public LootActionsBuilderJS thenAddWeighted(ItemStackJS[] itemStacks) {
        return thenAddWeighted(MinMaxBounds.Ints.atMost(1), true, itemStacks);
    }

    public LootActionsBuilderJS thenRemove(IngredientJS ingredient) {
        handlers.add(new RemoveLootAction(ingredient.getVanillaPredicate()));
        return this;
    }

    public LootActionsBuilderJS thenReplace(IngredientJS ingredient, ItemStackJS itemStack) {
        handlers.add(new ReplaceLootAction(ingredient.getVanillaPredicate(), itemStack.getItemStack()));
        return this;
    }

    public LootActionsBuilderJS thenModify(IngredientJS ingredient, Function<ItemStackJS, ItemStackJS> function) {
        handlers.add(new ModifyLootAction(ingredient.getVanillaPredicate(),
                (itemStack) -> function.apply(new ItemStackJS(itemStack)).getItemStack()));
        return this;
    }

    public LootActionsBuilderJS thenExplode(float radius, boolean destroy, boolean fire) {
        Explosion.BlockInteraction mode =
                destroy ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
        handlers.add(new ExplodeAction(radius, mode, fire));
        return this;
    }

    public LootActionsBuilderJS thenLightningStrike(boolean shouldDamage) {
        handlers.add(new LightningStrikeAction(shouldDamage));
        return this;
    }

    public LootActionsBuilderJS thenRollPool(Consumer<LootActionsBuilderJS> callback) {
        return thenRollPool(MinMaxBounds.Ints.exactly(1), callback);
    }

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
    public LootActionsBuilderJS addCondition(LootItemCondition condition) {
        handlers.add((ILootHandler) condition);
        return this;
    }
}
