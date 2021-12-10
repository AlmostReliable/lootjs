package com.github.llytho.lootjs.kube;

import com.github.llytho.lootjs.action.*;
import com.github.llytho.lootjs.core.IAction;
import com.github.llytho.lootjs.kube.action.CustomJSAction;
import com.github.llytho.lootjs.kube.condition.IConditionBuilder;
import com.github.llytho.lootjs.kube.context.LootContextJS;
import dev.latvian.kubejs.item.ItemStackJS;
import dev.latvian.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.LootConditionManager;
import net.minecraft.world.Explosion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CompositeLootActionBuilder implements IConditionBuilder<CompositeLootActionBuilder> {
    private final List<ConditionalAction> actions = new ArrayList<ConditionalAction>();
    private final List<Predicate<LootContext>> conditions = new ArrayList<Predicate<LootContext>>();

    public CompositeLootActionBuilder thenApply(Consumer<LootContextJS> pAction) {
        buildCurrentAction(new CustomJSAction(pAction));
        return this;
    }

    public CompositeLootActionBuilder thenAdd(ItemStackJS... pIngredient) {
        ItemStack[] itemStacks = Arrays.stream(pIngredient).map(ItemStackJS::getItemStack).toArray(ItemStack[]::new);
        buildCurrentAction(new AddLootAction(itemStacks));
        return this;
    }

    public CompositeLootActionBuilder thenRemove(IngredientJS pIngredient) {
        buildCurrentAction(new RemoveLootAction(pIngredient.getVanillaPredicate()));
        return this;
    }

    public CompositeLootActionBuilder thenReplace(IngredientJS pIngredient, ItemStackJS pItemStack) {
        buildCurrentAction(new ReplaceLootAction(pIngredient.getVanillaPredicate(), pItemStack.getItemStack()));
        return this;
    }

    public CompositeLootActionBuilder thenExplode(float pRadius, boolean pDestroy, boolean pFire) {
        Explosion.Mode mode = pDestroy ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
        buildCurrentAction(new ExplodeAction(pRadius, mode, pFire));
        return this;
    }

    public CompositeLootActionBuilder thenLightningStrike(boolean pShouldDamage) {
        buildCurrentAction(new LightningStrikeAction(pShouldDamage));
        return this;
    }

    @SuppressWarnings("unchecked")
    public void buildCurrentAction(IAction<LootContext> pAction) {
        Predicate<LootContext>[] conditionsArray = (Predicate<LootContext>[]) conditions.toArray(new Predicate[0]);
        Predicate<LootContext> predicate = LootConditionManager.andConditions(conditionsArray);
        conditions.clear();
        ConditionalAction action = new ConditionalAction(pAction, predicate);
        actions.add(action);
    }

    @HideFromJS
    public CompositeAction build() {
        if (actions.isEmpty()) {
            throw new IllegalArgumentException("No actions were added to the modifier");
        }

        ConditionalAction[] actionsArray = new ConditionalAction[actions.size()];
        return new CompositeAction(actions.toArray(actionsArray));
    }

    @Override
    public CompositeLootActionBuilder addCondition(ILootCondition pCondition) {
        conditions.add(pCondition);
        return this;
    }
}
