package com.almostreliable.lootjs.filters;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public abstract class Resolver {
    protected final ResourceLocation value;

    Resolver(ResourceLocation value) {
        this.value = value;
    }

    public static Resolver of(String value) {
        if (value.startsWith("#")) {
            return new ByTagKey(new ResourceLocation(value.substring(1)));
        }
        return new ByEntry(new ResourceLocation(value));
    }

    public static class ByEntry extends Resolver {
        ByEntry(ResourceLocation value) {
            super(value);
        }

        public <T> T resolve(Registry<T> registry) {
            T t = registry.get(value);
            if (t == null) {
                throw new IllegalArgumentException("No entry found for " + value);
            }
            return t;
        }

        public <T> ResourceKey<T> resolve(ResourceKey<Registry<T>> resourceKey) {
            return ResourceKey.create(resourceKey, value);
        }
    }

    public static class ByTagKey extends Resolver {
        ByTagKey(ResourceLocation value) {
            super(value);
        }

        public <T> TagKey<T> resolve(Registry<T> registry) {
            TagKey<T> tTagKey = TagKey.create(registry.key(), value);
            registry.getTag(tTagKey).orElseThrow(() -> new IllegalArgumentException("Tag not found: " + value));
            return tTagKey;
        }

        public <T> TagKey<T> resolve(ResourceKey<Registry<T>> resourceKey) {
            return TagKey.create(resourceKey, value);
        }
    }
}
