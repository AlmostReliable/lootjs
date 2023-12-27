package com.almostreliable.lootjs.mixin;

import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@RemapPrefixForJS("lootjs$")
@Mixin(LocationPredicate.Builder.class)
public abstract class LocationPredicateBuilderMixin {

    @HideFromJS
    @Shadow
    public abstract LocationPredicate.Builder setBiome(@Nullable ResourceKey<Biome> biome);

    @HideFromJS
    @Shadow
    public abstract LocationPredicate.Builder setStructure(@Nullable ResourceKey<Structure> structure);

    @HideFromJS
    @Shadow
    public abstract LocationPredicate.Builder setDimension(@Nullable ResourceKey<Level> dimension);

    @Unique
    public LocationPredicate.Builder lootjs$setBiome(@Nullable ResourceLocation biome) {
        if (biome == null) {
            return setBiome(null);
        }

        return setBiome(ResourceKey.create(Registries.BIOME, biome));
    }

    @Unique
    public LocationPredicate.Builder lootjs$setStructure(@Nullable ResourceLocation structure) {
        if (structure == null) {
            return setStructure(null);
        }

        return setStructure(ResourceKey.create(Registries.STRUCTURE, structure));
    }

    @Unique
    public LocationPredicate.Builder lootjs$setDimension(@Nullable ResourceLocation dimension) {
        if (dimension == null) {
            return setDimension(null);
        }

        return setDimension(ResourceKey.create(Registries.DIMENSION, dimension));
    }
}
