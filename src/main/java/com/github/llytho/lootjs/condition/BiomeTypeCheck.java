package com.github.llytho.lootjs.condition;

import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class BiomeTypeCheck extends ValueCondition<BiomeDictionary.Type, Set<BiomeDictionary.Type>> {

    private final List<BiomeDictionary.Type> types;

    public BiomeTypeCheck(List<BiomeDictionary.Type> types, IConditionOp.Factory<BiomeDictionary.Type, Set<BiomeDictionary.Type>> pFunc) {
        super(pFunc);
        this.types = types;
    }

    @Override
    protected boolean match(BiomeDictionary.Type type, Set<BiomeDictionary.Type> types) {
        return types.contains(type);
    }

    @Nullable
    @Override
    protected Collection<BiomeDictionary.Type> getLeftIterableValue(LootContext context) {
        return types;
    }

    @Nullable
    @Override
    protected Set<BiomeDictionary.Type> getRightValue(LootContext context) {
        Vector3d origin = context.getParamOrNull(LootParameters.ORIGIN);
        if (origin == null) return null;

        BlockPos blockPos = new BlockPos(origin.x, origin.y, origin.z);
        Biome biome = context.getLevel().getBiome(blockPos);
        if (biome.getRegistryName() == null) {
            return null;
        }

        RegistryKey<Biome> registryKey = RegistryKey.create(Registry.BIOME_REGISTRY, biome.getRegistryName());
        return BiomeDictionary.getTypes(registryKey);
    }
}
