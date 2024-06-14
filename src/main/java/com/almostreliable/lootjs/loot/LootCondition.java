package com.almostreliable.lootjs.loot;

import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class LootCondition implements LootConditionsContainer<LootItemCondition> {

    @Override
    public LootItemCondition addCondition(LootItemCondition condition) {
        return condition;
    }
}
