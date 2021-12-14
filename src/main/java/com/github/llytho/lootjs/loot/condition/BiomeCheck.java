package com.github.llytho.lootjs.loot.condition;

import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.List;
import java.util.Set;

public class BiomeCheck implements IExtendedLootCondition {
    protected final List<RegistryKey<Biome>> biomes;
    protected final List<BiomeDictionary.Type> types;

    public BiomeCheck(List<RegistryKey<Biome>> biomes, List<BiomeDictionary.Type> types) {
        this.biomes = biomes;
        this.types = types;
    }

    @Override
    public boolean test(LootContext context) {
        Vector3d origin = context.getParamOrNull(LootParameters.ORIGIN);
        if (origin == null) return false;

        BlockPos blockPos = new BlockPos(origin.x, origin.y, origin.z);
        Biome biome = context.getLevel().getBiome(blockPos);
        if (biome.getRegistryName() == null) {
            return false;
        }

        RegistryKey<Biome> ctxBiomeKey = RegistryKey.create(Registry.BIOME_REGISTRY, biome.getRegistryName());
        Set<BiomeDictionary.Type> ctxBiomeTypes = BiomeDictionary.getTypes(ctxBiomeKey);
        return match(ctxBiomeKey, ctxBiomeTypes);
    }

    protected boolean match(RegistryKey<Biome> biomeKey, Set<BiomeDictionary.Type> biomeTypes) {
        for (RegistryKey<Biome> biome : biomes) {
            if (biome != biomeKey) {
                return false;
            }
        }

        for (BiomeDictionary.Type type : types) {
            if (!biomeTypes.contains(type)) {
                return false;
            }
        }

        return true;
    }
}
