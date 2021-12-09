package com.github.llytho.lootjs.kube;

import com.github.llytho.lootjs.action.CompositeLootAction;
import com.github.llytho.lootjs.action.ConditionalLootAction;
import com.github.llytho.lootjs.action.RemoveLootAction;
import com.github.llytho.lootjs.action.ReplaceLootAction;
import com.github.llytho.lootjs.kube.action.CustomJSAction;
import com.github.llytho.lootjs.kube.context.LootContextJS;
import dev.latvian.kubejs.item.ItemStackJS;
import dev.latvian.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.LootConditionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CompositeLootActionBuilder implements IConditionBuilder<CompositeLootActionBuilder> {
    private final List<ConditionalLootAction> actions = new ArrayList<ConditionalLootAction>();
    private final List<Predicate<LootContext>> conditions = new ArrayList<Predicate<LootContext>>();

    public void thenApply(Consumer<LootContextJS> pAction) {
        buildCurrentAction(new CustomJSAction(pAction));
    }

    public void thenRemove(IngredientJS pIngredient) {
        buildCurrentAction(new RemoveLootAction(pIngredient.getVanillaPredicate()));
    }

    public void thenReplace(IngredientJS pIngredient, ItemStackJS pItemStack) {
        buildCurrentAction(new ReplaceLootAction(pIngredient.getVanillaPredicate(), pItemStack.getItemStack()));
    }

    @SuppressWarnings("unchecked")
    public void buildCurrentAction(Consumer<LootContext> pAction) {
        Predicate<LootContext>[] conditionsArray = (Predicate<LootContext>[]) conditions.toArray(new Predicate[0]);
        Predicate<LootContext> predicate = LootConditionManager.andConditions(conditionsArray);
        conditions.clear();
        ConditionalLootAction action = new ConditionalLootAction(pAction, predicate);
        actions.add(action);
    }

    @HideFromJS
    public CompositeLootAction build() {
        if (actions.isEmpty()) {
            throw new IllegalArgumentException("No actions were added to the modifier");
        }

        ConditionalLootAction[] actionsArray = new ConditionalLootAction[actions.size()];
        return new CompositeLootAction(actions.toArray(actionsArray));
    }

    @Override
    public CompositeLootActionBuilder addCondition(Predicate<LootContext> pCondition) {
        conditions.add(pCondition);
        return this;
    }
}
