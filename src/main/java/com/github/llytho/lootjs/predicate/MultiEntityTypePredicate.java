package com.github.llytho.lootjs.predicate;

import com.google.gson.JsonElement;
import net.minecraft.advancements.criterion.EntityTypePredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.tags.ITag;

import java.util.List;

public class MultiEntityTypePredicate extends EntityTypePredicate {

    private final List<EntityType<?>> types;
    private final List<ITag<EntityType<?>>> tags;

    public MultiEntityTypePredicate(List<ITag<EntityType<?>>> tags, List<EntityType<?>> types) {
        this.tags = tags;
        this.types = types;
    }

    @Override
    public boolean matches(EntityType<?> typeToCheck) {
        for (EntityType<?> type : types) {
            if (type == typeToCheck) {
                return true;
            }
        }

        for (ITag<EntityType<?>> tag : tags) {
            if (tag.contains(typeToCheck)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public JsonElement serializeToJson() {
        throw new UnsupportedOperationException("Not supported for custom predicates from LootJS mod");
    }
}
