package com.almostreliable.lootjs.fabric.core;

import com.almostreliable.lootjs.fabric.kubejs.LootConsumer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public interface FabricLootContextExtension {
    ResourceLocation lootjs$getQueriedLootTableId();

    void lootjs$setQueriedLootTableId(ResourceLocation id);

    @Nullable
    LootConsumer lootjs$getLootConsumer();

    void lootjs$setLootConsumer(LootConsumer consumer);
}
