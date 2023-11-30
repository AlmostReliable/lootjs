package com.almostreliable.lootjs.core;

import com.google.common.collect.ImmutableMap;
import net.minecraft.Util;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.Map;

public class LootContextParamSetsMapping {
    public static final Map<LootContextParamSet, LootType> PSETS_TO_TYPE = ImmutableMap
            .<LootContextParamSet, LootType>builder()
            .put(LootContextParamSets.BLOCK, LootType.BLOCK)
            .put(LootContextParamSets.ENTITY, LootType.ENTITY)
            .put(LootContextParamSets.CHEST, LootType.CHEST)
            .put(LootContextParamSets.FISHING, LootType.FISHING)
            .put(LootContextParamSets.GIFT, LootType.GIFT)
            .put(LootContextParamSets.ARCHAEOLOGY, LootType.ARCHAEOLOGY)
            .put(LootContextParamSets.PIGLIN_BARTER, LootType.PIGLIN_BARTER)
            .put(LootContextParamSets.ADVANCEMENT_ENTITY, LootType.ADVANCEMENT_ENTITY)
            .put(LootContextParamSets.ADVANCEMENT_REWARD, LootType.ADVANCEMENT_REWARD)
            .build();

    public static final Map<LootType, LootContextParamSet> TYPE_TO_PSETS = Util.make(() -> {
        var builder = ImmutableMap.<LootType, LootContextParamSet>builder();
        for (var entry : PSETS_TO_TYPE.entrySet()) {
            builder.put(entry.getValue(), entry.getKey());
        }

        return builder.build();
    });
}
