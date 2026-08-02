package com.almostreliable.lootjs.kube.wrappers;

import com.almostreliable.lootjs.core.entry.ItemLootEntry;
import com.almostreliable.lootjs.core.entry.LootEntry;
import dev.latvian.mods.kubejs.script.ConsoleJS;
import dev.latvian.mods.rhino.Context;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

public class LootEntryWrapper {

    public static ItemLootEntry ofItemLootEntry(Context cx, @Nullable Object o) {
        return switch (o) {
            case ItemLootEntry e -> e;
            case String str -> {
                var item = BuiltInRegistries.ITEM.getOptional(Identifier.parse(str)).orElse(null);
                if (item == null) {
                    ConsoleJS.SERVER.error("[LootEntry.of()] Invalid item identifier, returning empty stack: " + o);
                    ConsoleJS.SERVER.error(
                            "- Consider using `LootEntry.empty()` if you want to create an empty loot entry.");
                    yield LootEntry.of(Items.AIR);
                }

                yield LootEntry.of(item);
            }
            case ItemLike item -> LootEntry.of(item.asItem());
            case null, default -> {
                ConsoleJS.SERVER.error("[LootEntry.of()] Invalid item identifier, returning empty stack: " + o);
                ConsoleJS.SERVER.error(
                        "- Consider using `LootEntry.empty()` if you want to create an empty loot entry.");
                yield LootEntry.of(Items.AIR);
            }
        };
    }

    public static LootEntry ofLootEntry(Context cx, @Nullable Object o) {
        if (o instanceof LootEntry entry) {
            return entry;
        }

        if (o instanceof String str && str.startsWith("#")) {
            String tag = str.substring(0, 1);
            return LootEntry.tag(tag, false);
        }

        return ofItemLootEntry(cx, o);
    }
}
