package com.github.llytho.lootjs.condition;

import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public class BiomeCheck extends ValueCondition<RegistryKey<Biome>, RegistryKey<Biome>> {
    private final List<RegistryKey<Biome>> biomes;

    public BiomeCheck(List<RegistryKey<Biome>> pBiomes, IConditionOp.Factory<RegistryKey<Biome>, RegistryKey<Biome>> pFunc) {
        super(pFunc);
        biomes = pBiomes;
    }

    @Override
    protected boolean match(RegistryKey<Biome> biomeThis, RegistryKey<Biome> biomeFromContext) {
        return biomeThis == biomeFromContext;
    }

    @Nullable
    @Override
    protected Collection<RegistryKey<Biome>> getLeftIterableValue(LootContext context) {
        return biomes;
    }

    @Nullable
    @Override
    protected RegistryKey<Biome> getRightValue(LootContext context) {
        Vector3d origin = context.getParamOrNull(LootParameters.ORIGIN);
        if (origin == null) return null;

        BlockPos blockPos = new BlockPos(origin.x, origin.y, origin.z);
        Biome biome = context.getLevel().getBiome(blockPos);
        if (biome.getRegistryName() == null) {
            return null;
        }

        return RegistryKey.create(Registry.BIOME_REGISTRY, biome.getRegistryName());
    }
}
