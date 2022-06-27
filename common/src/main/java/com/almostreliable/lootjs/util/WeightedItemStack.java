package com.almostreliable.lootjs.util;

import net.minecraft.world.item.ItemStack;

public class WeightedItemStack {
    private final int weight;
    private final ItemStack itemStack;

    public WeightedItemStack(ItemStack itemStack, int weight) {
        this.itemStack = itemStack.copy();
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
