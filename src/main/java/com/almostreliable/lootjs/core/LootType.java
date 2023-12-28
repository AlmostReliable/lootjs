package com.almostreliable.lootjs.core;

import net.minecraft.Util;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.HashMap;
import java.util.Map;

public enum LootType {
    UNKNOWN(new LootContextParamSet.Builder().build()),
    BLOCK(LootContextParamSets.BLOCK),
    ENTITY(LootContextParamSets.ENTITY),
    CHEST(LootContextParamSets.CHEST),
    FISHING(LootContextParamSets.FISHING),
    GIFT(LootContextParamSets.GIFT),
    ARCHAEOLOGY(LootContextParamSets.ARCHAEOLOGY),
    PIGLIN_BARTER(LootContextParamSets.PIGLIN_BARTER),
    ADVANCEMENT_ENTITY(LootContextParamSets.ADVANCEMENT_ENTITY),
    ADVANCEMENT_REWARD(LootContextParamSets.ADVANCEMENT_REWARD),
    ADVANCEMENT_LOCATION(LootContextParamSets.ADVANCEMENT_LOCATION),
    COMMAND(LootContextParamSets.COMMAND),
    SELECTOR(LootContextParamSets.SELECTOR),
    GENERIC(LootContextParamSets.ALL_PARAMS);

    private static final Map<LootContextParamSet, LootType> MAPPINGS = Util.make(new HashMap<>(), (m) -> {
        for (LootType lootType : values()) {
            m.put(lootType.getParamSet(), lootType);
        }
    });

    public static LootType getLootType(LootContextParamSet paramSet) {
        return MAPPINGS.getOrDefault(paramSet, LootType.UNKNOWN);
    }

    private final LootContextParamSet paramSet;

    LootType(LootContextParamSet paramSet) {
        this.paramSet = paramSet;
    }

    public LootContextParamSet getParamSet() {
        return this.paramSet;
    }
}
