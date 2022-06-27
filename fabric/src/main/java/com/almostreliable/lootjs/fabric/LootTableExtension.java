package com.almostreliable.lootjs.fabric;

import net.minecraft.resources.ResourceLocation;

public interface LootTableExtension {
    ResourceLocation lootjs$getLootTableId();

    void lootjs$setLootTableId(ResourceLocation id);
}
