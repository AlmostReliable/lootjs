package com.almostreliable.lootjs.util;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import javax.annotation.Nullable;
import java.util.*;

public interface EntityTypeFilter {

    static EntityTypeFilter of(@Nullable Object o) {
        if (o instanceof EntityTypeFilter) {
            return (EntityTypeFilter) o;
        }

        if (o instanceof List<?> list) {
            List<EntityTypeFilter> filters = new ArrayList<>();
            for (Object object : list) {
                filters.add(of(object));
            }

            return new OrFilter(filters);
        }

        if (o instanceof EntityType<?> type) {
            return new Basic(type);
        }

        if (o instanceof TagKey<?> tag) {
            if (tag.registry() != Registries.ENTITY_TYPE) {
                throw new IllegalArgumentException("Provided tag is not an entity type tag: " + tag);
            }

            //noinspection unchecked
            return new TagFilter((TagKey<EntityType<?>>) tag);
        }

        if (o instanceof String str) {
            if (str.startsWith("#")) {
                ResourceLocation tag = new ResourceLocation(str.substring(1));
                return of(TagKey.create(Registries.ENTITY_TYPE, tag));
            }

            return of(BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(str)));
        }

        if (o instanceof ResourceLocation rl) {
            return of(BuiltInRegistries.ENTITY_TYPE.get(rl));
        }


        throw new IllegalArgumentException("Invalid entity filter: " + o);
    }

    boolean test(EntityType<?> e);

    Set<EntityType<?>> getEntityTypes();

    record Basic(EntityType<?> type) implements EntityTypeFilter {

        @Override
        public boolean test(EntityType<?> e) {
            return e == type;
        }

        @Override
        public Set<EntityType<?>> getEntityTypes() {
            return Collections.singleton(type);
        }
    }

    class TagFilter implements EntityTypeFilter {

        private final TagKey<EntityType<?>> tag;
        @Nullable
        private Set<EntityType<?>> entityTypes;

        public TagFilter(TagKey<EntityType<?>> tag) {
            this.tag = tag;
        }

        @Override
        public boolean test(EntityType<?> e) {
            return e.is(tag);
        }

        @Override
        public Set<EntityType<?>> getEntityTypes() {
            if (entityTypes == null) {
                entityTypes = new HashSet<>();
                for (Holder<EntityType<?>> h : BuiltInRegistries.ENTITY_TYPE.getTagOrEmpty(tag)) {
                    entityTypes.add(h.value());
                }
            }

            return Collections.unmodifiableSet(entityTypes);
        }
    }

    class OrFilter implements EntityTypeFilter {

        private final List<EntityTypeFilter> filters;
        @Nullable
        private Set<EntityType<?>> entityTypes;

        public OrFilter(List<EntityTypeFilter> filters) {
            this.filters = filters;
        }

        @Override
        public boolean test(EntityType<?> e) {
            for (EntityTypeFilter filter : filters) {
                if (filter.test(e)) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public Set<EntityType<?>> getEntityTypes() {
            if (entityTypes == null) {
                entityTypes = new HashSet<>();
                for (EntityTypeFilter filter : filters) {
                    entityTypes.addAll(filter.getEntityTypes());
                }
            }

            return entityTypes;
        }
    }
}
