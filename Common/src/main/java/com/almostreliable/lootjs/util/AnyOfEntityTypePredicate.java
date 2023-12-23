package com.almostreliable.lootjs.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.world.entity.EntityType;

import java.util.Collection;

public class AnyOfEntityTypePredicate extends EntityTypePredicate {
    public static final EntityTypePredicate EMPTY = new EntityTypePredicate() {
        @Override
        public boolean matches(EntityType<?> type) {
            return false;
        }

        @Override
        public JsonElement serializeToJson() {
            return new JsonObject();
        }
    };

    private final Collection<EntityTypePredicate> predicates;

    public AnyOfEntityTypePredicate(Collection<EntityTypePredicate> predicates) {
        this.predicates = predicates;
    }

    @Override
    public boolean matches(EntityType<?> type) {
        for (EntityTypePredicate predicate : predicates) {
            if (predicate.matches(type)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public JsonElement serializeToJson() {
        JsonArray arr = new JsonArray();
        for (EntityTypePredicate predicate : predicates) {
            arr.add(predicate.serializeToJson());
        }

        return arr;
    }
}
