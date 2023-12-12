package com.almostreliable.lootjs.core;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import javax.annotation.Nullable;

@FunctionalInterface
public interface ItemStackFactory {

    ItemStackFactory EMPTY = context -> null;

    @Nullable
    ItemStack create(LootContext context);
}
