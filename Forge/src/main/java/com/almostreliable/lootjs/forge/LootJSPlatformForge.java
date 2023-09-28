package com.almostreliable.lootjs.forge;

import com.almostreliable.lootjs.LootJSPlatform;
import com.almostreliable.lootjs.forge.filters.ForgeItemFilter;
import com.almostreliable.lootjs.forge.mixin.LootPoolFactory;
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
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;

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
        return Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block));
    }

    @Override
    public ResourceLocation getRegistryName(EntityType<?> entityType) {
        return Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(entityType));
    }

    @Override
    public ResourceLocation getQueriedLootTableId(LootContext context) {
        return context.getQueriedLootTableId();
    }

    @Override
    public void setQueriedLootTableId(LootContext context, ResourceLocation id) {
        context.setQueriedLootTableId(id);
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("ForgeItemFilter", ForgeItemFilter.class);
    }

    @Override
    public LootPool createLootPool(LootPoolEntryContainer[] entries, LootItemCondition[] conditions, LootItemFunction[] functions, NumberProvider rolls, NumberProvider bonusRolls, @Nullable String name) {
        return LootPoolFactory.create(entries, conditions, functions, rolls, bonusRolls, name);
    }
}
