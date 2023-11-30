package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.core.LootConditionTypes;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public interface IExtendedLootCondition extends LootItemCondition {
    @Override
    default LootItemConditionType getType() {
        return LootConditionTypes.UNUSED;
    }
}
