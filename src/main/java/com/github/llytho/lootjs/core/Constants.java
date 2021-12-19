package com.github.llytho.lootjs.core;

import com.google.common.collect.ImmutableMap;
import net.minecraft.loot.LootParameter;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class Constants {
    public static final String MODID = "lootjs";

    public static final LootParameter<ILootContextData> DATA = new LootParameter<>(new ResourceLocation(MODID,
            "param_data"));

    public static final LootParameter<LootModificationDebug> RESULT_LOGGER = new LootParameter<>(new ResourceLocation(
            MODID,
            "result_logger"));

    public static final Map<LootParameterSet, LootContextType> PSETS_TO_TYPE = ImmutableMap
            .<LootParameterSet, LootContextType>builder()
            .put(LootParameterSets.BLOCK, LootContextType.BLOCK)
            .put(LootParameterSets.ENTITY, LootContextType.ENTITY)
            .put(LootParameterSets.CHEST, LootContextType.CHEST)
            .put(LootParameterSets.FISHING, LootContextType.FISHING)
            .put(LootParameterSets.GIFT, LootContextType.GIFT)
            .put(LootParameterSets.PIGLIN_BARTER, LootContextType.PIGLIN_BARTER)
            .put(LootParameterSets.ADVANCEMENT_ENTITY, LootContextType.ADVANCEMENT_ENTITY)
            .put(LootParameterSets.ADVANCEMENT_REWARD, LootContextType.ADVANCEMENT_REWARD)
            .build();
}
