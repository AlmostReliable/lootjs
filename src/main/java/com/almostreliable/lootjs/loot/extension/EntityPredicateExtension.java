package com.almostreliable.lootjs.loot.extension;

import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;

@RemapPrefixForJS("lootjs$")
public interface EntityPredicateExtension {

    EntityFlagsPredicate.Builder lootjs$getFlagsBuilder();

    EntityPredicate.Builder lootjs$self();

    default EntityPredicate.Builder lootjs$setOnFire(boolean onFire) {
        lootjs$getFlagsBuilder().setOnFire(onFire);
        return lootjs$self();
    }

    default EntityPredicate.Builder lootjs$setCrouching(boolean isCrouching) {
        lootjs$getFlagsBuilder().setCrouching(isCrouching);
        return lootjs$self();
    }

    default EntityPredicate.Builder lootjs$setSprinting(boolean isSprinting) {
        lootjs$getFlagsBuilder().setSprinting(isSprinting);
        return lootjs$self();
    }

    default EntityPredicate.Builder lootjs$setSwimming(boolean isSwimming) {
        lootjs$getFlagsBuilder().setSwimming(isSwimming);
        return lootjs$self();
    }

    default EntityPredicate.Builder lootjs$setIsBaby(boolean isBaby) {
        lootjs$getFlagsBuilder().setIsBaby(isBaby);
        return lootjs$self();
    }
}
