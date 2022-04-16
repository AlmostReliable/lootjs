package com.github.llytho.lootjs.loot.condition;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.List;

public class AnyBiomeCheck extends BiomeCheck {

    public AnyBiomeCheck(List<ResourceKey<Biome>> biomes, List<TagKey<Biome>> tags) {
        super(biomes, tags);
    }

    protected boolean match(Holder<Biome> biomeHolder) {
        for (ResourceKey<Biome> biome : biomes) {
            if (biomeHolder.is(biome)) {
                return true;
            }
        }

        for (TagKey<Biome> tag : tags) {
            if(biomeHolder.is(tag)) {
                return true;
            }
        }

        return false;
    }
}
