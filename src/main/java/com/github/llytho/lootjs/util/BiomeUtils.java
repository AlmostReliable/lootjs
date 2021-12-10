package com.github.llytho.lootjs.util;

import com.github.llytho.lootjs.core.FilterResult;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class BiomeUtils {
    public static List<BiomeDictionary.Type> findTypes(String[] pTypes) {
        FilterResult<String, BiomeDictionary.Type> filterResult = FilterResult.create(pTypes, (pFilter) -> {
            return BiomeDictionary.Type
                    .getAll()
                    .stream()
                    .filter(type -> type.getName().equalsIgnoreCase(pFilter))
                    .findFirst()
                    .orElse(null);
        });

        filterResult.yeetIfUnresolvedFilters();

        return filterResult.getFoundValues();
    }

    @SuppressWarnings("unchecked")
    public static List<RegistryKey<Biome>> findBiomeKeys(ResourceLocation[] pBiomes) {
        FilterResult<ResourceLocation, RegistryKey<Biome>> filterResult = FilterResult.create(pBiomes, (pLocation) -> {
            if (ForgeRegistries.BIOMES.getValue(pLocation) == null) {
                return null;
            }
            return RegistryKey.create(Registry.BIOME_REGISTRY, pLocation);
        });


        return filterResult.getFoundValues();
    }

    public static Structure<?>[] findStructures(ResourceLocation[] pBiomes) {
        FilterResult<ResourceLocation, Structure<?>> filterResult = FilterResult.create(pBiomes,
                ForgeRegistries.STRUCTURE_FEATURES::getValue);

        filterResult.yeetIfUnresolvedFilters();

        return (Structure<?>[]) filterResult.getFoundValues().toArray(new Structure[0]);
    }
}
