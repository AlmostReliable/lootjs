package com.github.llytho.lootjs.condition;

import com.github.llytho.lootjs.core.ICondition;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;

public class BiomeCheck extends ValueCondition<RegistryKey<Biome>, RegistryKey<Biome>> {
    public BiomeCheck(RegistryKey<Biome>[] pValues, ICondition<RegistryKey<Biome>, RegistryKey<Biome>> pCondition) {
        super(pValues, pCondition);
    }

    @Override
    protected boolean match(RegistryKey<Biome> pBiomeCheck, RegistryKey<Biome> pBiomeThis) {
        return pBiomeCheck == pBiomeThis;
    }

    @Nullable
    @Override
    protected RegistryKey<Biome> getValue(LootContext pContext) {
        Vector3d origin = pContext.getParamOrNull(LootParameters.ORIGIN);
        if (origin == null) return null;

        BlockPos blockPos = new BlockPos(origin.x, origin.y, origin.z);
        Biome biome = pContext.getLevel().getBiome(blockPos);
        if (biome.getRegistryName() == null) {
            return null;
        }

        return RegistryKey.create(Registry.BIOME_REGISTRY, biome.getRegistryName());
    }
}
