package com.almostreliable.lootjs.mixin;

import dev.latvian.mods.rhino.util.RemapForJS;
import net.minecraft.advancements.criterion.DistancePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@SuppressWarnings({ "unused", "OptionalUsedAsFieldOrParameterType" })
@Mixin(EntityPredicate.class)
public abstract class EntityPredicateMixin {
    @RemapForJS("distance")
    @Shadow
    @Final
    private Optional<DistancePredicate> distanceToPlayer;

    @RemapForJS("distance")
    @Shadow
    public abstract Optional<DistancePredicate> distanceToPlayer();
}
