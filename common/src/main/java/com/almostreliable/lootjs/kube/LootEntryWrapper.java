package com.almostreliable.lootjs.kube;

import com.almostreliable.lootjs.core.LootEntry;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import net.minecraft.nbt.CompoundTag;

public class LootEntryWrapper {
    public static LootEntry of(Object o) {
        if (o instanceof LootEntry entry) {
            return entry;
        }

        ItemStackJS itemStackJS = ItemStackJS.of(o);
        int weight = itemStackJS.hasChance() ? (int) itemStackJS.getChance() : 1;
        return new LootEntry(itemStackJS.getItemStack()).withWeight(weight);
    }

    public static LootEntry of(ItemStackJS in, int count) {
        return of(in.withCount(count));
    }

    public static LootEntry of(ItemStackJS in, CompoundTag nbt) {
        return of(in.withNBT(nbt));
    }

    public static LootEntry of(ItemStackJS in, int count, CompoundTag nbt) {
        return of(in.withCount(count).withNBT(nbt));
    }

    public static LootEntry withChance(Object o, int chance) {
        return of(o).withChance(chance);
    }
}
