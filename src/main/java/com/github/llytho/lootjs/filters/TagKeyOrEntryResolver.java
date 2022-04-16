package com.github.llytho.lootjs.filters;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public abstract class TagKeyOrEntryResolver {
    protected final ResourceLocation value;

    TagKeyOrEntryResolver(ResourceLocation value) {
        this.value = value;
    }

    public static TagKeyOrEntryResolver of(String value) {
        if (value.startsWith("#")) {
            return new ByTagKey(new ResourceLocation(value.substring(1)));
        }
        return new ByEntry(new ResourceLocation(value));
    }

    public static class ByEntry extends TagKeyOrEntryResolver {
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
    }

    public static class ByTagKey extends TagKeyOrEntryResolver {
        ByTagKey(ResourceLocation value) {
            super(value);
        }

        public <T>TagKey<T> resolve(Registry<T> registry) {
            TagKey<T> tTagKey = TagKey.create(registry.key(), value);
            registry.getTag(tTagKey).orElseThrow(() -> new IllegalArgumentException("Tag not found: " + value));
            return tTagKey;
        }
    }
}
