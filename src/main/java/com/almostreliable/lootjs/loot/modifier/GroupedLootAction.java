package com.almostreliable.lootjs.loot.modifier;

import com.almostreliable.lootjs.core.LootBucket;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.loot.LootActionContainer;
import com.almostreliable.lootjs.loot.LootConditionsContainer;
import com.almostreliable.lootjs.loot.LootFunctionsContainer;
import net.minecraft.Util;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class GroupedLootAction implements LootAction {

    private final List<LootAction> actions;
    private final ItemFilter containsLootFilter;
    private final boolean exact;
    private final Predicate<LootContext> compositeCondition;
    private final BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;
    private final NumberProvider rolls;
    private final List<LootItemFunction> functions;

    public GroupedLootAction(NumberProvider rolls, List<LootItemCondition> conditions, List<LootItemFunction> functions, Collection<LootAction> actions, ItemFilter containsLootFilter, boolean exact) {
        this.rolls = rolls;
        this.compositeCondition = Util.allOf(List.copyOf(conditions));
        this.functions = List.copyOf(functions);
        this.compositeFunction = LootItemFunctions.compose(this.functions);
        this.actions = List.copyOf(actions);
        this.containsLootFilter = containsLootFilter;
        this.exact = exact;
    }

    @Override
    public void apply(LootContext context, LootBucket loot) {
        boolean containsLoot = exact ? matchExact(loot) : match(loot);
        if (!containsLoot) {
            return;
        }

        int r = rolls.getInt(context);
        for (int i = 0; i < r; i++) {
            if (!compositeCondition.test(context)) {
                continue;
            }

            for (var action : actions) {
                action.apply(context, loot);
            }

            if (!functions.isEmpty()) {
                loot.modifyItems(itemStack -> compositeFunction.apply(itemStack, context));
            }
        }
    }

    private boolean match(LootBucket loot) {
        for (ItemStack itemStack : loot) {
            if (containsLootFilter.test(itemStack)) {
                return true;
            }
        }

        return false;
    }

    private boolean matchExact(LootBucket loot) {
        for (ItemStack itemStack : loot) {
            if (!containsLootFilter.test(itemStack)) {
                return false;
            }
        }

        return true;
    }

    public static class Filtered extends GroupedLootAction {

        private final ItemFilter lootItemsFilter;

        public Filtered(NumberProvider rolls, List<LootItemCondition> conditions, List<LootItemFunction> functions, Collection<LootAction> actions, ItemFilter lootItemsFilter, ItemFilter containsLootFilter, boolean exact) {
            super(rolls, conditions, functions, actions, containsLootFilter, exact);
            this.lootItemsFilter = lootItemsFilter;
        }

        @Override
        public void apply(LootContext context, LootBucket loot) {
            LootBucket filteredLoot = loot.extract(lootItemsFilter);
            super.apply(context, filteredLoot);
            loot.merge(filteredLoot);
        }
    }

    public static class Builder implements LootConditionsContainer<Builder>,
                                           LootFunctionsContainer<Builder>,
                                           LootActionContainer<Builder> {

        protected final List<LootAction> actions = new ArrayList<>();
        protected final List<LootItemCondition> conditions = new ArrayList<>();
        protected final List<LootItemFunction> functions = new ArrayList<>();
        protected NumberProvider rolls = ConstantValue.exactly(1f);
        protected ItemFilter containsLootFilter = ItemFilter.ANY;
        protected boolean exact = false;
        @Nullable
        private final ItemFilter lootItemsFilter;

        public Builder(ItemFilter lootItemsFilter) {
            this.lootItemsFilter = lootItemsFilter;
        }

        public Builder() {
            this.lootItemsFilter = null;
        }

        public Builder rolls(NumberProvider rolls) {
            this.rolls = rolls;
            return this;
        }

        public Builder containsLoot(ItemFilter filter) {
            this.containsLootFilter = filter;
            return this;
        }

        public Builder containsLoot(ItemFilter filter, boolean exact) {
            this.containsLootFilter = filter;
            this.exact = exact;
            return this;
        }

        @Override
        public Builder addCondition(LootItemCondition condition) {
            conditions.add(condition);
            return this;
        }

        @Override
        public Builder addAction(LootAction action) {
            actions.add(action);
            return this;
        }

        @Override
        public Builder addFunction(LootItemFunction lootItemFunction) {
            functions.add(lootItemFunction);
            return this;
        }

        public GroupedLootAction build() {
            if (lootItemsFilter != null) {
                return new Filtered(rolls, conditions, functions, actions, lootItemsFilter, containsLootFilter, exact);
            }

            return new GroupedLootAction(rolls, conditions, functions, actions, containsLootFilter, exact);
        }
    }
}
