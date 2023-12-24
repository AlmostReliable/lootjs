package com.almostreliable.lootjs.mixin;

import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.MobEffectsPredicate;
import net.minecraft.world.effect.MobEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@RemapPrefixForJS("lootjs$")
@Mixin(MobEffectsPredicate.class)
public abstract class MobEffectsPredicateMixin {

    @Shadow
    public abstract MobEffectsPredicate and(MobEffect effect, MobEffectsPredicate.MobEffectInstancePredicate predicate);

    @Unique
    public MobEffectsPredicate lootjs$and(MobEffect effect, MinMaxBounds.Ints amplifier) {
        return lootjs$and(effect, amplifier, MinMaxBounds.Ints.ANY);
    }

    @Unique
    public MobEffectsPredicate lootjs$and(MobEffect effect, MinMaxBounds.Ints amplifier, MinMaxBounds.Ints duration) {
        return lootjs$and(effect, amplifier, duration, null);
    }

    @Unique
    public MobEffectsPredicate lootjs$and(MobEffect effect, MinMaxBounds.Ints amplifier, MinMaxBounds.Ints duration, @Nullable Boolean ambient) {
        return lootjs$and(effect, amplifier, duration, ambient, null);
    }

    @Unique
    public MobEffectsPredicate lootjs$and(MobEffect effect, MinMaxBounds.Ints amplifier, MinMaxBounds.Ints duration, @Nullable Boolean ambient, @Nullable Boolean visible) {
        var ip = new MobEffectsPredicate.MobEffectInstancePredicate(amplifier, duration, ambient, visible);
        return this.and(effect, ip);
    }
}
