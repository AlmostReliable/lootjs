package com.github.llytho.lootjs.kube.builder;

import com.github.llytho.lootjs.core.LootAction;
import com.github.llytho.lootjs.kube.ConditionsContainer;
import com.github.llytho.lootjs.kube.LootContextJS;
import com.github.llytho.lootjs.kube.action.CustomJSAction;
import com.github.llytho.lootjs.loot.action.*;
import dev.latvian.kubejs.item.ItemStackJS;
import dev.latvian.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.world.Explosion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class LootActionsBuilderJS implements ConditionsContainer<LootActionsBuilderJS> {
    private final List<LootAction> actions = new ArrayList<>();
    private final List<ILootCondition> conditions = new ArrayList<>();

    public LootActionsBuilderJS thenApply(Consumer<LootContextJS> action) {
        buildCurrentAction(new CustomJSAction(action));
        return this;
    }

    public LootActionsBuilderJS thenAdd(ItemStackJS... ingredient) {
        ItemStack[] itemStacks = Arrays.stream(ingredient).map(ItemStackJS::getItemStack).toArray(ItemStack[]::new);
        buildCurrentAction(new AddLootAction(itemStacks));
        return this;
    }

    public LootActionsBuilderJS thenRemove(IngredientJS ingredient) {
        buildCurrentAction(new RemoveLootAction(ingredient.getVanillaPredicate()));
        return this;
    }

    public LootActionsBuilderJS thenReplace(IngredientJS ingredient, ItemStackJS itemStack) {
        buildCurrentAction(new ReplaceLootAction(ingredient.getVanillaPredicate(), itemStack.getItemStack()));
        return this;
    }

    public LootActionsBuilderJS thenExplode(float radius, boolean destroy, boolean fire) {
        Explosion.Mode mode = destroy ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
        buildCurrentAction(new ExplodeAction(radius, mode, fire));
        return this;
    }

    public LootActionsBuilderJS thenLightningStrike(boolean shouldDamage) {
        buildCurrentAction(new LightningStrikeAction(shouldDamage));
        return this;
    }

    public void buildCurrentAction(LootAction action) {
        ILootCondition[] conditionsArray = conditions.toArray(new ILootCondition[0]);
        conditions.clear();
        ConditionalAction conditionalAction = new ConditionalAction(action, conditionsArray);
        actions.add(conditionalAction);
    }

    @HideFromJS
    public List<LootAction> getActions() {
        if (actions.isEmpty()) {
            throw new IllegalArgumentException("No actions were added to the modifier");
        }

        return actions;
    }

    @Override
    public LootActionsBuilderJS addCondition(ILootCondition pCondition) {
        conditions.add(pCondition);
        return this;
    }
}
