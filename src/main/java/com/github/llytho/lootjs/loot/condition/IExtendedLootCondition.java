package com.github.llytho.lootjs.loot.condition;

import com.github.llytho.lootjs.core.ILootCondition;
import com.github.llytho.lootjs.core.LootConditionTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.List;

public interface IExtendedLootCondition extends ILootCondition, LootItemCondition {
    @Override
    default LootItemConditionType getType() {
        return LootConditionTypes.UNUSED;
    }

    @Override
    default boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        return test(context);
    }
}
