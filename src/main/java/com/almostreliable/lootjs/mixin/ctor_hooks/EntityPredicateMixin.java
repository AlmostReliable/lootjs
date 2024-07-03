package com.almostreliable.lootjs.mixin.ctor_hooks;

import net.minecraft.advancements.critereon.EntityPredicate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(EntityPredicate.class)
public class EntityPredicateMixin {

    @Mutable @Shadow @Final private EntityPredicate.LocationWrapper location;

    @SuppressWarnings({ "OptionalUsedAsFieldOrParameterType", "rawtypes" })
    @Inject(method = "<init>", at = @At("RETURN"))
    private void lootjs$setDefaults(Optional entityType, Optional distanceToPlayer, Optional movement, EntityPredicate.LocationWrapper location, Optional effects, Optional nbt, Optional flags, Optional equipment, Optional subPredicate, Optional periodicTick, Optional vehicle, Optional passenger, Optional targetedEntity, Optional team, Optional slots, CallbackInfo ci) {
        if (this.location == null) {
            this.location = new EntityPredicate.LocationWrapper(Optional.empty(), Optional.empty(), Optional.empty());
        }
    }
}
