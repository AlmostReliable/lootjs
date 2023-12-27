package com.almostreliable.lootjs.loot.extension;

import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public interface LootItemConditionalFunctionExtension {
    List<LootItemCondition> lootjs$getConditions();

    void lootjs$setConditions(List<LootItemCondition> conditions);
}
