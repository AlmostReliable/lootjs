package com.almostreliable.lootjs.kube;

import com.almostreliable.lootjs.core.LootEntry;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.OutputItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class LootEntryWrapper {
    public static LootEntry of(Object o) {
        if (o instanceof LootEntry entry) {
            return entry;
        }

        OutputItem outputItem = OutputItem.of(o);
        int weight = Double.isNaN(outputItem.getChance()) ?  1 : (int) outputItem.getChance();
        return new LootEntry(outputItem.item).withWeight(weight);
    }

    public static LootEntry of(ItemStack in, int count) {
        return of(in.kjs$withCount(count));
    }

    public static LootEntry of(ItemStack in, CompoundTag nbt) {
        return of(in.kjs$withNBT(nbt));
    }

    public static LootEntry of(ItemStack in, int count, CompoundTag nbt) {
        return of(in.kjs$withCount(count).kjs$withNBT(nbt));
    }

    public static LootEntry withChance(Object o, int chance) {
        return of(o).withChance(chance);
    }
}
