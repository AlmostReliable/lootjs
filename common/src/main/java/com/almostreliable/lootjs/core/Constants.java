package com.almostreliable.lootjs.core;

import com.almostreliable.lootjs.BuildConfig;
import com.almostreliable.lootjs.loot.results.LootInfoCollector;
import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.Map;

public class Constants {
    public static final LootContextParam<ILootContextData> DATA = new LootContextParam<>(new ResourceLocation(
            BuildConfig.MOD_ID,
            "param_data"));

    public static final LootContextParam<DebugStack> RESULT_LOGGER = new LootContextParam<>(new ResourceLocation(
            BuildConfig.MOD_ID,
            "result_logger"));

    public static final LootContextParam<LootInfoCollector> RESULT_COLLECTOR = new LootContextParam<>(new ResourceLocation(
            BuildConfig.MOD_ID,
            "result_collector"));

    public static final Map<LootContextParamSet, LootContextType> PSETS_TO_TYPE = ImmutableMap
            .<LootContextParamSet, LootContextType>builder()
            .put(LootContextParamSets.BLOCK, LootContextType.BLOCK)
            .put(LootContextParamSets.ENTITY, LootContextType.ENTITY)
            .put(LootContextParamSets.CHEST, LootContextType.CHEST)
            .put(LootContextParamSets.FISHING, LootContextType.FISHING)
            .put(LootContextParamSets.GIFT, LootContextType.GIFT)
            .put(LootContextParamSets.PIGLIN_BARTER, LootContextType.PIGLIN_BARTER)
            .put(LootContextParamSets.ADVANCEMENT_ENTITY, LootContextType.ADVANCEMENT_ENTITY)
            .put(LootContextParamSets.ADVANCEMENT_REWARD, LootContextType.ADVANCEMENT_REWARD)
            .build();
}
