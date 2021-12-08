package com.github.llytho.lootjs.core;

import net.minecraft.loot.LootContext;

public interface ILootModifier {
    void apply(LootContext pContext);
}
