package com.almostreliable.lootjs.core;

import com.almostreliable.lootjs.BuildConfig;
import com.almostreliable.lootjs.loot.results.LootInfoCollector;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;

public class LootJSParamSets {
    public static final LootContextParam<LootInfoCollector> RESULT_COLLECTOR = new LootContextParam<>(new ResourceLocation(
            BuildConfig.MOD_ID,
            "result_collector"));

}
