package com.almostreliable.lootjs.core;

import net.minecraft.util.Util;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.HashMap;
import java.util.Map;

public enum LootType {
    UNKNOWN(new ContextKeySet.Builder().build()),
    BLOCK(LootContextParamSets.BLOCK),
    BLOCK_USE(LootContextParamSets.BLOCK_USE),
    CHEST(LootContextParamSets.CHEST),
    FISHING(LootContextParamSets.FISHING),
    ENTITY(LootContextParamSets.ENTITY),
    EQUIPMENT(LootContextParamSets.EQUIPMENT),
    ARCHAEOLOGY(LootContextParamSets.ARCHAEOLOGY),
    GIFT(LootContextParamSets.GIFT),
    VAULT(LootContextParamSets.VAULT),
    PIGLIN_BARTER(LootContextParamSets.PIGLIN_BARTER),
    ADVANCEMENT_REWARD(LootContextParamSets.ADVANCEMENT_REWARD),
    ADVANCEMENT_ENTITY(LootContextParamSets.ADVANCEMENT_ENTITY),
    ADVANCEMENT_LOCATION(LootContextParamSets.ADVANCEMENT_LOCATION),
    COMMAND(LootContextParamSets.COMMAND),
    SELECTOR(LootContextParamSets.SELECTOR),
    SHEARING(LootContextParamSets.SHEARING),
    GENERIC(LootContextParamSets.ALL_PARAMS);

    private static final Map<ContextKeySet, LootType> MAPPINGS = Util.make(new HashMap<>(), (m) -> {
        for (LootType lootType : values()) {
            m.put(lootType.getParamSet(), lootType);
        }
    });

    public static LootType getLootType(ContextKeySet paramSet) {
        return MAPPINGS.getOrDefault(paramSet, LootType.UNKNOWN);
    }

    private final ContextKeySet paramSet;

    LootType(ContextKeySet paramSet) {
        this.paramSet = paramSet;
    }

    public ContextKeySet getParamSet() {
        return this.paramSet;
    }
}
