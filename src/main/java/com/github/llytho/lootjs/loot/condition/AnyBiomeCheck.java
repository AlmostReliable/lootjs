package com.github.llytho.lootjs.loot.condition;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.List;
import java.util.Set;

public class AnyBiomeCheck extends BiomeCheck {

    public AnyBiomeCheck(List<ResourceKey<Biome>> biomes, List<BiomeDictionary.Type> types) {
        super(biomes, types);
    }

    protected boolean match(ResourceKey<Biome> biomeKey, Set<BiomeDictionary.Type> biomeTypes) {
        for (ResourceKey<Biome> biome : biomes) {
            if (biome == biomeKey) {
                return true;
            }
        }

        for (BiomeDictionary.Type type : types) {
            if (biomeTypes.contains(type)) {
                return true;
            }
        }

        return false;
    }
}
