package com.github.llytho.lootjs.core;

import net.minecraft.loot.LootContext;

public interface ILootModification {
    String getName();

    boolean shouldExecute(LootContext context);

    boolean execute(LootContext context);
}
