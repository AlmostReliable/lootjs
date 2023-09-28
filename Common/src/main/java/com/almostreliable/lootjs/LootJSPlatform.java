package com.almostreliable.lootjs;

import dev.latvian.mods.kubejs.script.BindingsEvent;
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

    LootPool createLootPool(LootPoolEntryContainer[] entries, LootItemCondition[] conditions, LootItemFunction[] functions, NumberProvider rolls, NumberProvider bonusRolls, @Nullable String name);
}
