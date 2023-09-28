package com.almostreliable.lootjs.fabric;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public interface LootTableIdOwner {
    @Nullable
    ResourceLocation lootjs$getLootTableId();

    void lootjs$setLootTableId(ResourceLocation id);
}
