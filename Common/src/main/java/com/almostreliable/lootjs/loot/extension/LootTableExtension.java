package com.almostreliable.lootjs.loot.extension;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

import javax.annotation.Nullable;
import java.util.List;

public interface LootTableExtension {

    List<LootPool> lootjs$getPools();

    void lootjs$setPools(List<LootPool> pools);

    LootItemFunction[] lootjs$getFunctions();

    void lootjs$setFunctions(LootItemFunction[] functions);

    void lootjs$setRandomSequence(@Nullable ResourceLocation randomSequence);

    @Nullable
    ResourceLocation lootjs$getRandomSequence();

    void lootjs$setParamSet(LootContextParamSet paramSet);

    LootContextParamSet lootjs$getParamSet();
}
