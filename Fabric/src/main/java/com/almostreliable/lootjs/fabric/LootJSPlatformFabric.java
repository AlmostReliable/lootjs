package com.almostreliable.lootjs.fabric;

import com.almostreliable.lootjs.LootJSPlatform;
import com.almostreliable.lootjs.fabric.core.FabricLootContextExtension;
import com.almostreliable.lootjs.fabric.mixin.LootPoolFactory;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import javax.annotation.Nullable;

public class LootJSPlatformFabric implements LootJSPlatform {
    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public ResourceLocation getRegistryName(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    @Override
    public ResourceLocation getRegistryName(EntityType<?> entityType) {
        return BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
    }

    @Override
    public ResourceLocation getQueriedLootTableId(LootContext context) {
        return ((FabricLootContextExtension) context).lootjs$getQueriedLootTableId();
    }

    @Override
    public void setQueriedLootTableId(LootContext context, ResourceLocation id) {
        ((FabricLootContextExtension) context).lootjs$setQueriedLootTableId(id);
    }

    @Override
    public void registerBindings(BindingsEvent event) {

    }

    @Override
    public LootPool createLootPool(LootPoolEntryContainer[] entries, LootItemCondition[] conditions, LootItemFunction[] functions, NumberProvider rolls, NumberProvider bonusRolls, @Nullable String name) {
        return LootPoolFactory.create(entries, conditions, functions, rolls, bonusRolls);
    }
}
