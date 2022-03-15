package com.github.llytho.lootjs.loot.condition;

import com.github.llytho.lootjs.core.ILootHandler;
import com.github.llytho.lootjs.core.LootConditionTypes;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public interface IExtendedLootCondition extends LootItemCondition, ILootHandler {
    @Override
    default LootItemConditionType getType() {
        return LootConditionTypes.UNUSED;
    }
}
