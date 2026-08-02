package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.LootJSConditions;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;

public record MatchBiome(HolderSet<Biome> biomes) implements LootItemCondition {

    @Override
    public boolean test(LootContext context) {
        Vec3 origin = context.getOptionalParameter(LootContextParams.ORIGIN);
        if (origin == null) return false;

        BlockPos blockPos = new BlockPos((int) origin.x, (int) origin.y, (int) origin.z);
        Holder<Biome> biome = context.getLevel().getBiome(blockPos);
        return biomes.contains(biome);
    }

    @Override
    public MapCodec<? extends LootItemCondition> codec() {
        return LootJSConditions.BIOME.value();
    }
}
