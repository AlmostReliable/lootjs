package com.almostreliable.lootjs.core;

import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class LootConditionTypes {
    public static final LootItemConditionType UNUSED = create();

    private static LootItemConditionType create() {
        // TODO
        return new LootItemConditionType(null);
    }
}
