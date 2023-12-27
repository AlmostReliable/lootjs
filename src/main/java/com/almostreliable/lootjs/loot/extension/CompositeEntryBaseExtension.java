package com.almostreliable.lootjs.loot.extension;

import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

import java.util.List;

public interface CompositeEntryBaseExtension {
    List<LootPoolEntryContainer> lootjs$getEntries();

    void lootjs$setEntries(List<LootPoolEntryContainer> children);
}
