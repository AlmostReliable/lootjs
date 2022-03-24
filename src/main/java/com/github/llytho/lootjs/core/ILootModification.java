package com.github.llytho.lootjs.core;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;

public interface ILootModification {
    String getName();

    boolean shouldExecute(LootContext context);

    boolean execute(LootContext context, List<ItemStack> loot);
}
