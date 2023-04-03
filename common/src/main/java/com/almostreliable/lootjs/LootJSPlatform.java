package com.almostreliable.lootjs;

import com.almostreliable.lootjs.filters.ItemFilter;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;

public interface LootJSPlatform {

    LootJSPlatform INSTANCE = PlatformLoader.load(LootJSPlatform.class);

    String getPlatformName();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();

    ResourceLocation getRegistryName(Block block);

    ResourceLocation getRegistryName(EntityType<?> entityType);

    ResourceLocation getQueriedLootTableId(LootContext context);

    void setQueriedLootTableId(LootContext context, ResourceLocation id);

    void registerBindings(BindingsEvent event);
}
