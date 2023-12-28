package com.almostreliable.lootjs.core.entry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;

public class TableReferenceLootEntry extends AbstractSimpleLootEntry<LootTableReference> {
    public TableReferenceLootEntry(LootTableReference vanillaEntry) {
        super(vanillaEntry);
    }

    public TableReferenceLootEntry(ResourceLocation location) {
        super(new LootTableReference(location,
                LootPoolSingletonContainer.DEFAULT_WEIGHT,
                LootPoolSingletonContainer.DEFAULT_QUALITY,
                EMPTY_CONDITIONS,
                EMPTY_FUNCTIONS));
    }

    public ResourceLocation getLocation() {
        return vanillaEntry.name;
    }

    public void setLocation(ResourceLocation reference) {
        vanillaEntry.name = reference;
    }
}
