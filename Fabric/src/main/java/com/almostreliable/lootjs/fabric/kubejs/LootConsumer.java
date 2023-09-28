package com.almostreliable.lootjs.fabric.kubejs;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LootConsumer implements Consumer<ItemStack> {

    private final List<ItemStack> loot = new ArrayList<>();
    private final Consumer<ItemStack> originConsumer;

    public LootConsumer(Consumer<ItemStack> originConsumer) {
        this.originConsumer = originConsumer;
    }

    @Override
    public void accept(ItemStack itemStack) {
        loot.add(itemStack);
    }

    public List<ItemStack> getLoot() {
        return loot;
    }

    public void resolve() {
        loot.forEach(originConsumer);
    }
}
