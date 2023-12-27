package com.almostreliable.lootjs.mixin;


import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@RemapPrefixForJS("lootjs$")
@Mixin(DamageSourcePredicate.Builder.class)
public abstract class DamageSourcePredicateBuilderMixin {
    @HideFromJS
    @Shadow
    public abstract DamageSourcePredicate.Builder tag(TagPredicate<DamageType> tag);

    @Unique
    public DamageSourcePredicate.Builder lootjs$is(ResourceLocation tag) {
        return this.tag(TagPredicate.is(TagKey.create(Registries.DAMAGE_TYPE, tag)));
    }

    @Unique
    public DamageSourcePredicate.Builder lootjs$isNot(ResourceLocation tag) {
        return this.tag(TagPredicate.isNot(TagKey.create(Registries.DAMAGE_TYPE, tag)));
    }
}
