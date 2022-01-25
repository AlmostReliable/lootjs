package com.github.llytho.lootjs.loot.condition;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.BiomeDictionary;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BiomeCheck implements IExtendedLootCondition {
    protected final List<ResourceKey<Biome>> biomes;
    protected final List<BiomeDictionary.Type> types;

    public BiomeCheck(List<ResourceKey<Biome>> biomes, List<BiomeDictionary.Type> types) {
        this.biomes = biomes;
        this.types = types;
    }

    @Override
    public boolean test(LootContext context) {
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        if (origin == null) return false;

        BlockPos blockPos = new BlockPos(origin.x, origin.y, origin.z);
        Biome biome = context.getLevel().getBiome(blockPos);
        if (biome.getRegistryName() == null) {
            return false;
        }

        ResourceKey<Biome> ctxBiomeKey = ResourceKey.create(Registry.BIOME_REGISTRY, biome.getRegistryName());
        Set<BiomeDictionary.Type> ctxBiomeTypes = BiomeDictionary.getTypes(ctxBiomeKey);
        return match(ctxBiomeKey, ctxBiomeTypes);
    }

    protected boolean match(ResourceKey<Biome> biomeKey, Set<BiomeDictionary.Type> biomeTypes) {
        for (ResourceKey<Biome> biome : biomes) {
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

    public List<ResourceKey<Biome>> getBiomes() {
        return Collections.unmodifiableList(biomes);
    }

    public List<BiomeDictionary.Type> getTypes() {
        return Collections.unmodifiableList(types);
    }
}
