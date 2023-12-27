package com.almostreliable.lootjs.loot.table;

import com.almostreliable.lootjs.LootJS;
import com.almostreliable.lootjs.core.entry.*;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;

public interface LootAppendHelper {

    default LootEntry addCustomEntry(JsonObject json) {
        var vanilla = LootPoolEntries.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(false, LootJS.LOG::error);
        return addAndReturn(LootEntry.ofVanilla(vanilla));
    }

    default ItemLootEntry addItem(ItemStack itemStack) {
        return addAndReturn(LootEntry.of(itemStack));
    }

    default TagLootEntry addTag(String tag) {
        return addAndReturn(LootEntry.tag(tag, false));
    }

    default TagLootEntry addTag(String tag, boolean expand) {
        return addAndReturn(LootEntry.tag(tag, expand));
    }

    default TableReferenceLootEntry addReference(ResourceLocation lootTable) {
        return addAndReturn(LootEntry.reference(lootTable));
    }

    default EmptyLootEntry addEmpty() {
        return addAndReturn(LootEntry.empty());
    }

    default CompositeLootEntry addAlternative(LootEntry... entries) {
        return addAndReturn(LootEntry.alternative(entries));
    }

    default CompositeLootEntry addSequence(LootEntry... entries) {
        return addAndReturn(LootEntry.sequence(entries));
    }

    default CompositeLootEntry addGroup(LootEntry... entries) {
        return addAndReturn(LootEntry.group(entries));
    }

    private <T extends LootEntry> T addAndReturn(T entry) {
        this.addEntry(entry);
        return entry;
    }

    void addEntry(LootEntry entry);
}
