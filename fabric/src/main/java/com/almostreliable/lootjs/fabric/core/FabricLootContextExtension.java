package com.almostreliable.lootjs.fabric.core;

import net.minecraft.resources.ResourceLocation;

public interface FabricLootContextExtension {
    ResourceLocation lootjs$getQueriedLootTableId();

    void lootjs$setQueriedLootTableId(ResourceLocation id);
}
