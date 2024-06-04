package com.almostreliable.lootjs.core.entry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class DynamicLootEntry extends AbstractSimpleLootEntry<DynamicLoot> {
    public DynamicLootEntry(DynamicLoot vanillaEntry) {
        super(vanillaEntry);
    }

    public ResourceLocation getLocation() {
        return vanillaEntry.name;
    }

    public void setLocation(ResourceLocation reference) {
        vanillaEntry.name = reference;
    }

    @Override
    public DynamicLootEntry addCondition(LootItemCondition condition) {
        getConditions().add(condition);
        return this;
    }
}
