package com.github.llytho.lootjs.core;

import net.minecraft.loot.LootContext;

public interface ILootAction {
    boolean accept(LootContext context);
}
