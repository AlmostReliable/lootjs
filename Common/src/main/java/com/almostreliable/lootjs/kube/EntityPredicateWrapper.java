package com.almostreliable.lootjs.kube;

import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;

public class EntityPredicateWrapper {

    public static EntityPredicate.Builder of(EntityTypePredicate entityTypePredicate) {
        return EntityPredicate.Builder.entity().entityType(entityTypePredicate);
    }
}
