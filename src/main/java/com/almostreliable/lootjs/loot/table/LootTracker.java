package com.almostreliable.lootjs.loot.table;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LootTracker implements Consumer<ItemStack> {

    private final List<ItemStack> loot = new ArrayList<>();
    private final Consumer<ItemStack> originConsumer;
    @Nullable
    private final ResourceLocation tableId;

    public LootTracker(Consumer<ItemStack> originConsumer, @Nullable ResourceLocation tableId) {
        this.originConsumer = originConsumer;
        this.tableId = tableId;
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

    @Nullable
    public ResourceLocation getTableId() {
        return tableId;
    }

    @Override
    public String toString() {
        return "LootTracker[" + tableId + "] " + loot.size() + "x items";
    }
}
