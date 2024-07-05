package com.almostreliable.lootjs.kube.wrappers;

import com.almostreliable.lootjs.core.entry.ItemLootEntry;
import com.almostreliable.lootjs.core.entry.LootEntry;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.script.ConsoleJS;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class LootEntryWrapper {

    public static ItemLootEntry ofItemLootEntry(RegistryAccessContainer registries, @Nullable Object o) {
        if (o instanceof ItemLootEntry e) {
            return e;
        }

        ItemStack itemStack = ItemStackJS.wrap(registries, o);
        if (itemStack.isEmpty()) {
            ConsoleJS.SERVER.error("[LootEntry.of()] Invalid item stack, returning empty stack: " + o);
            ConsoleJS.SERVER.error("- Consider using `LootEntry.empty()` if you want to create an empty loot entry.");
            return LootEntry.of(ItemStack.EMPTY);
        }

        return LootEntry.of(itemStack);
    }

    public static LootEntry ofLootEntry(RegistryAccessContainer registries, @Nullable Object o) {
        if (o instanceof LootEntry entry) {
            return entry;
        }

        if (o instanceof String str && str.startsWith("#")) {
            String tag = str.substring(0, 1);
            return LootEntry.tag(tag, false);
        }

        return ofItemLootEntry(registries, o);
    }
}
