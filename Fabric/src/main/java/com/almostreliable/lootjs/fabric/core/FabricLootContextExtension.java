package com.almostreliable.lootjs.fabric.core;

import com.almostreliable.lootjs.fabric.kubejs.LootConsumer;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public interface FabricLootContextExtension {
    ResourceLocation lootjs$getQueriedLootTableId();

    @HideFromJS
    void lootjs$setQueriedLootTableId(ResourceLocation id);

    @HideFromJS
    @Nullable
    LootConsumer lootjs$getLootConsumer();

    @HideFromJS
    void lootjs$setLootConsumer(LootConsumer consumer);
}
