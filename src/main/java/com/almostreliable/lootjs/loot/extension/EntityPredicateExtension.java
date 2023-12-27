package com.almostreliable.lootjs.loot.extension;

import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;

import javax.annotation.Nullable;

@RemapPrefixForJS("lootjs$")
public interface EntityPredicateExtension {

    EntityFlagsPredicate.Builder lootjs$getFlagsBuilder();

    EntityPredicate.Builder lootjs$self();

    default EntityPredicate.Builder lootjs$setOnFire(@Nullable Boolean onFire) {
        lootjs$getFlagsBuilder().setOnFire(onFire);
        return lootjs$self();
    }

    default EntityPredicate.Builder lootjs$setCrouching(@Nullable Boolean isCrouching) {
        lootjs$getFlagsBuilder().setCrouching(isCrouching);
        return lootjs$self();
    }

    default EntityPredicate.Builder lootjs$setSprinting(@Nullable Boolean isSprinting) {
        lootjs$getFlagsBuilder().setSprinting(isSprinting);
        return lootjs$self();
    }

    default EntityPredicate.Builder lootjs$setSwimming(@Nullable Boolean isSwimming) {
        lootjs$getFlagsBuilder().setSwimming(isSwimming);
        return lootjs$self();
    }

    default EntityPredicate.Builder lootjs$setIsBaby(@Nullable Boolean isBaby) {
        lootjs$getFlagsBuilder().setIsBaby(isBaby);
        return lootjs$self();
    }
}
