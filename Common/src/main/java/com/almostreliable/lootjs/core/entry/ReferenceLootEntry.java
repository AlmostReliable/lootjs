package com.almostreliable.lootjs.core.entry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;

public class ReferenceLootEntry extends AbstractSimpleLootEntry<LootTableReference> {
    public ReferenceLootEntry(LootTableReference vanillaEntry) {
        super(vanillaEntry);
    }

    public ReferenceLootEntry(ResourceLocation location) {
        super(new LootTableReference(location,
                LootPoolSingletonContainer.DEFAULT_WEIGHT,
                LootPoolSingletonContainer.DEFAULT_QUALITY,
                LootEntry.EMPTY_CONDITIONS,
                LootEntry.EMPTY_FUNCTIONS));
    }

    public ResourceLocation getLocation() {
        return vanillaEntry.name;
    }

    public void setLocation(ResourceLocation reference) {
        vanillaEntry.name = reference;
    }
}
