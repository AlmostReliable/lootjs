package com.almostreliable.lootjs.core;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;

public interface ILootHandler {
    boolean applyLootHandler(LootContext context, List<ItemStack> loot);
}
