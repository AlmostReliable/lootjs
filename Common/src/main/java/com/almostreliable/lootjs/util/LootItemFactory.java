package com.almostreliable.lootjs.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.function.Consumer;

public interface LootItemFactory {

    boolean createItem(Consumer<ItemStack> consumer, LootContext context);
}
