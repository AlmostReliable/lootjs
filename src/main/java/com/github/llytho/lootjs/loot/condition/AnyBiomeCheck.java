package com.github.llytho.lootjs.loot.condition;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.List;
import java.util.Set;

public class AnyBiomeCheck extends BiomeCheck {

    public AnyBiomeCheck(List<RegistryKey<Biome>> biomes, List<BiomeDictionary.Type> types) {
        super(biomes, types);
    }

    protected boolean match(RegistryKey<Biome> biomeKey, Set<BiomeDictionary.Type> biomeTypes) {
        for (RegistryKey<Biome> biome : biomes) {
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
