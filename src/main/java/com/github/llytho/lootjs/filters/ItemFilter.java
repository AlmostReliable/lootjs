package com.github.llytho.lootjs.filters;

import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public interface ItemFilter extends Predicate<ItemStack> {
    ItemFilter ALWAYS_FALSE = itemStack -> false;
    ItemFilter ALWAYS_TRUE = itemStack -> true;
}
