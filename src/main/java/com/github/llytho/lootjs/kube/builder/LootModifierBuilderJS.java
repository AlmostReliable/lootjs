package com.github.llytho.lootjs.kube.builder;

import com.github.llytho.lootjs.core.IAction;
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
import net.minecraft.loot.conditions.LootConditionManager;
import net.minecraft.world.Explosion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class LootModifierBuilderJS implements ConditionsContainer<LootModifierBuilderJS> {
    private final List<ConditionalAction> actions = new ArrayList<ConditionalAction>();
    private final List<Predicate<LootContext>> conditions = new ArrayList<Predicate<LootContext>>();

    public LootModifierBuilderJS thenApply(Consumer<LootContextJS> action) {
        buildCurrentAction(new CustomJSAction(action));
        return this;
    }

    public LootModifierBuilderJS thenAdd(ItemStackJS... ingredient) {
        ItemStack[] itemStacks = Arrays.stream(ingredient).map(ItemStackJS::getItemStack).toArray(ItemStack[]::new);
        buildCurrentAction(new AddLootAction(itemStacks));
        return this;
    }

    public LootModifierBuilderJS thenRemove(IngredientJS ingredient) {
        buildCurrentAction(new RemoveLootAction(ingredient.getVanillaPredicate()));
        return this;
    }

    public LootModifierBuilderJS thenReplace(IngredientJS ingredient, ItemStackJS itemStack) {
        buildCurrentAction(new ReplaceLootAction(ingredient.getVanillaPredicate(), itemStack.getItemStack()));
        return this;
    }

    public LootModifierBuilderJS thenExplode(float radius, boolean destroy, boolean fire) {
        Explosion.Mode mode = destroy ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
        buildCurrentAction(new ExplodeAction(radius, mode, fire));
        return this;
    }

    public LootModifierBuilderJS thenLightningStrike(boolean shouldDamage) {
        buildCurrentAction(new LightningStrikeAction(shouldDamage));
        return this;
    }

    @SuppressWarnings("unchecked")
    public void buildCurrentAction(IAction<LootContext> action) {
        Predicate<LootContext>[] conditionsArray = (Predicate<LootContext>[]) conditions.toArray(new Predicate[0]);
        Predicate<LootContext> predicate = LootConditionManager.andConditions(conditionsArray);
        conditions.clear();
        ConditionalAction conditionalAction = new ConditionalAction(action, predicate);
        actions.add(conditionalAction);
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
    public LootModifierBuilderJS addCondition(ILootCondition pCondition) {
        conditions.add(pCondition);
        return this;
    }
}