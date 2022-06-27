package com.almostreliable.lootjs.loot.condition;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.List;

public class BiomeCheck implements IExtendedLootCondition {
    protected final List<ResourceKey<Biome>> biomes;
    protected final List<TagKey<Biome>> tags;

    public BiomeCheck(List<ResourceKey<Biome>> biomes, List<TagKey<Biome>> tags) {
        this.biomes = biomes;
        this.tags = tags;
    }

    @Override
    public boolean test(LootContext context) {
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        if (origin == null) return false;

        BlockPos blockPos = new BlockPos(origin.x, origin.y, origin.z);
        return match(context.getLevel().getBiome(blockPos));
    }

    protected boolean match(Holder<Biome> biomeHolder) {
        for (ResourceKey<Biome> biome : biomes) {
            if (!biomeHolder.is(biome)) {
                return false;
            }
        }

        for (TagKey<Biome> tag : tags) {
            if (!biomeHolder.is(tag)) {
                return false;
            }
        }

        return true;
    }

    public List<ResourceKey<Biome>> getBiomes() {
        return Collections.unmodifiableList(biomes);
    }

    public List<TagKey<Biome>> getTags() {
        return Collections.unmodifiableList(tags);
    }
}
