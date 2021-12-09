package com.github.llytho.lootjs.condition;

import com.github.llytho.lootjs.core.LootConditionTypes;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.conditions.ILootCondition;

public interface IExtendedLootCondition extends ILootCondition {
    @Override
    default LootConditionType getType() {
        return LootConditionTypes.UNUSED;
    }
}
