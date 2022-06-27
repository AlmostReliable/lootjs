package com.almostreliable.lootjs.loot.condition;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class LootItemConditionWrapper implements IExtendedLootCondition {

    protected final LootItemCondition condition;

    public LootItemConditionWrapper(LootItemCondition condition) {
        this.condition = condition;
    }

    @Override
    public boolean test(LootContext context) {
        return condition.test(context);
    }

    public LootItemCondition getCondition() {
        return condition;
    }
}
