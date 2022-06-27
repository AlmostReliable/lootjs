package com.almostreliable.lootjs.forge;

import com.almostreliable.lootjs.LootJSPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

public class LootJSPlatformForge implements LootJSPlatform {
    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public ResourceLocation getRegistryName(Block block) {
        return block.getRegistryName();
    }

    @Override
    public ResourceLocation getRegistryName(EntityType<?> entityType) {
        return entityType.getRegistryName();
    }

    @Override
    public ResourceLocation getQueriedLootTableId(LootContext context) {
        return context.getQueriedLootTableId();
    }

    @Override
    public void setQueriedLootTableId(LootContext context, ResourceLocation id) {
        context.setQueriedLootTableId(id);
    }
}
