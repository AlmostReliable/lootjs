package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.core.ILootAction;
import com.almostreliable.lootjs.core.ILootCondition;
import com.almostreliable.lootjs.core.ILootHandler;
import com.almostreliable.lootjs.kube.LootConditionsContainer;
import com.almostreliable.lootjs.loot.action.GroupedLootAction;
import com.almostreliable.lootjs.loot.action.LootItemFunctionWrapperAction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.ArrayList;
import java.util.List;

public class GroupedLootBuilder implements LootConditionsContainer<GroupedLootBuilder>,
                                           LootFunctionsContainer<GroupedLootBuilder>,
                                           LootActionsContainer<GroupedLootBuilder> {

    private final List<ILootHandler> handlers = new ArrayList<>();
    private NumberProvider numberProvider = ConstantValue.exactly(1f);

    public GroupedLootBuilder rolls(NumberProvider numberProvider) {
        this.numberProvider = numberProvider;
        return this;
    }

    @Override
    public GroupedLootBuilder addCondition(LootItemCondition condition) {
        handlers.add((ILootCondition) condition); // TODO Remove ILootCondition some day
        return this;
    }

    @Override
    public GroupedLootBuilder addAction(ILootAction action) {
        handlers.add(action);
        return this;
    }

    public GroupedLootAction build() {
        return new GroupedLootAction(numberProvider, handlers);
    }

    @Override
    public GroupedLootBuilder addFunction(LootItemFunction lootItemFunction) {
        return addAction(new LootItemFunctionWrapperAction(lootItemFunction));
    }
}
