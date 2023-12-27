package com.almostreliable.lootjs.mixin;

import com.almostreliable.lootjs.loot.extension.EntityPredicateExtension;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(EntityPredicate.Builder.class)
public abstract class EntityPredicateBuilderMixin implements EntityPredicateExtension {

    @Nullable
    @Unique
    private EntityFlagsPredicate.Builder lootjs$flagsPredicate;

    @HideFromJS
    @Shadow
    public abstract EntityPredicate.Builder flags(EntityFlagsPredicate flags);

    @Shadow private EntityFlagsPredicate flags;

    @Override
    public EntityPredicate.Builder lootjs$self() {
        return (EntityPredicate.Builder) (Object) this;
    }

    @Override
    public EntityFlagsPredicate.Builder lootjs$getFlagsBuilder() {
        if(lootjs$flagsPredicate == null) {
            lootjs$flagsPredicate = EntityFlagsPredicate.Builder.flags();
        }

        return lootjs$flagsPredicate;
    }

    @Inject(method = "build", at = @At("HEAD"))
    private void lootjs$replaceFlagsBuilder(CallbackInfoReturnable<EntityPredicate> cir) {
        if (lootjs$flagsPredicate != null) {
            this.flags = lootjs$flagsPredicate.build();
        }
    }
}
