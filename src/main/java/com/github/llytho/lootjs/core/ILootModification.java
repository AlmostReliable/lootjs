package com.github.llytho.lootjs.core;

import net.minecraft.world.level.storage.loot.LootContext;

public interface ILootModification {
    String getName();

    boolean shouldExecute(LootContext context);

    boolean execute(LootContext context);
}
