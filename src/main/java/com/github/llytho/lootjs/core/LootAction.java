package com.github.llytho.lootjs.core;

import net.minecraft.loot.LootContext;

public interface LootAction {
    boolean accept(LootContext context);
}
