package com.github.llytho.lootjs.condition;

import com.github.llytho.lootjs.core.ICondition;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nullable;
import java.util.Set;

public class BiomeTypeCheck extends ValueCondition<BiomeDictionary.Type, Set<BiomeDictionary.Type>> {
    public BiomeTypeCheck(BiomeDictionary.Type[] pValues, ICondition<BiomeDictionary.Type, Set<BiomeDictionary.Type>> pCondition) {
        super(pValues, pCondition);
    }

    @Override
    protected boolean match(Set<BiomeDictionary.Type> pTypes, BiomeDictionary.Type pType) {
        return pTypes.contains(pType);
    }

    @Nullable
    @Override
    protected Set<BiomeDictionary.Type> getValue(LootContext pContext) {
        Vector3d origin = pContext.getParamOrNull(LootParameters.ORIGIN);
        if (origin == null) return null;

        BlockPos blockPos = new BlockPos(origin.x, origin.y, origin.z);
        Biome biome = pContext.getLevel().getBiome(blockPos);
        if (biome.getRegistryName() == null) {
            return null;
        }

        RegistryKey<Biome> registryKey = RegistryKey.create(Registry.BIOME_REGISTRY, biome.getRegistryName());
        return BiomeDictionary.getTypes(registryKey);
    }
}
