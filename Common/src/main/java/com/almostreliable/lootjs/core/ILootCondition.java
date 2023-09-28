package com.almostreliable.lootjs.core;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;
import java.util.function.Predicate;

public interface ILootCondition extends ILootHandler, Predicate<LootContext> {
    @Override
    default boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        return test(context);
    }
}
