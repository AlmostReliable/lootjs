package com.github.llytho.lootjs.kube;

import com.github.llytho.lootjs.action.*;
import com.github.llytho.lootjs.core.IAction;
import com.github.llytho.lootjs.kube.action.CustomJSAction;
import com.github.llytho.lootjs.kube.condition.IConditionBuilder;
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

    public CompositeLootActionBuilder thenApply(Consumer<LootContextJS> action) {
        buildCurrentAction(new CustomJSAction(action));
        return this;
    }

    public CompositeLootActionBuilder thenAdd(ItemStackJS... ingredient) {
        ItemStack[] itemStacks = Arrays.stream(ingredient).map(ItemStackJS::getItemStack).toArray(ItemStack[]::new);
        buildCurrentAction(new AddLootAction(itemStacks));
        return this;
    }

    public CompositeLootActionBuilder thenRemove(IngredientJS ingredient) {
        buildCurrentAction(new RemoveLootAction(ingredient.getVanillaPredicate()));
        return this;
    }

    public CompositeLootActionBuilder thenReplace(IngredientJS ingredient, ItemStackJS itemStack) {
        buildCurrentAction(new ReplaceLootAction(ingredient.getVanillaPredicate(), itemStack.getItemStack()));
        return this;
    }

    public CompositeLootActionBuilder thenExplode(float radius, boolean destroy, boolean fire) {
        Explosion.Mode mode = destroy ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
        buildCurrentAction(new ExplodeAction(radius, mode, fire));
        return this;
    }

    public CompositeLootActionBuilder thenLightningStrike(boolean shouldDamage) {
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
    public CompositeLootActionBuilder addCondition(ILootCondition pCondition) {
        conditions.add(pCondition);
        return this;
    }
}
