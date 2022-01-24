package com.github.llytho.lootjs.util;

import com.github.llytho.lootjs.core.FilterResult;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class BiomeUtils {
    public static List<BiomeDictionary.Type> findTypes(List<String> types) {
        FilterResult<String, BiomeDictionary.Type> filterResult = FilterResult.create(types,
                (pFilter) -> BiomeDictionary.Type
                        .getAll()
                        .stream()
                        .filter(type -> type.getName().equalsIgnoreCase(pFilter))
                        .findFirst()
                        .orElse(null));

        filterResult.yeetIfUnresolvedFilters();
        return filterResult.getFoundValues();
    }

    @SuppressWarnings("unchecked")
    public static List<ResourceKey<Biome>> findBiomeKeys(List<ResourceLocation> biomes) {
        FilterResult<ResourceLocation, ResourceKey<Biome>> filterResult = FilterResult.create(biomes, (pLocation) -> {
            if (ForgeRegistries.BIOMES.getValue(pLocation) == null) {
                return null;
            }
            return ResourceKey.create(Registry.BIOME_REGISTRY, pLocation);
        });

        filterResult.yeetIfUnresolvedFilters();
        return filterResult.getFoundValues();
    }

    public static StructureFeature<?>[] findStructures(List<ResourceLocation> structures) {
        FilterResult<ResourceLocation, StructureFeature<?>> filterResult = FilterResult.create(structures,
                ForgeRegistries.STRUCTURE_FEATURES::getValue);

        filterResult.yeetIfUnresolvedFilters();
        return (StructureFeature<?>[]) filterResult.getFoundValues().toArray(new StructureFeature[0]);
    }
}
