package com.github.llytho.lootjs.loot;

import com.github.llytho.lootjs.core.ILootAction;
import com.github.llytho.lootjs.core.ILootCondition;
import com.github.llytho.lootjs.core.ILootHandler;
import com.github.llytho.lootjs.kube.ConditionsContainer;
import com.github.llytho.lootjs.loot.action.LootPoolAction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.ArrayList;
import java.util.List;

public class LootPoolBuilder implements ConditionsContainer<LootPoolBuilder>,
                                        LootFunctionsContainer<LootPoolBuilder>,
                                        LootActionsContainer<LootPoolBuilder> {

    private final List<ILootHandler> handlers = new ArrayList<>();
    private NumberProvider numberProvider = ConstantValue.exactly(1f);

    public LootPoolBuilder rolls(NumberProvider numberProvider) {
        this.numberProvider = numberProvider;
        return this;
    }

    @Override
    public LootPoolBuilder addCondition(ILootCondition condition) {
        handlers.add(condition);
        return this;
    }

    @Override
    public LootPoolBuilder addAction(ILootAction action) {
        handlers.add(action);
        return this;
    }

    public LootPoolAction build() {
        return new LootPoolAction(numberProvider, handlers);
    }
}
