package com.github.llytho.lootjs.core;

import net.minecraft.world.level.storage.loot.LootContext;

public interface ILootAction {
    boolean accept(LootContext context);
}
