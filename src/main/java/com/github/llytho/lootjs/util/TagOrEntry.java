package com.github.llytho.lootjs.util;

import net.minecraft.tags.ITag;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class TagOrEntry<T extends IForgeRegistryEntry<T>> {
    public final ITag<T> tag;
    public final T entry;

    TagOrEntry(ITag<T> tag) {
        this.tag = tag;
        this.entry = null;
    }

    TagOrEntry(T entry) {
        this.tag = null;
        this.entry = entry;
    }

    public static <T extends IForgeRegistryEntry<T>> TagOrEntry<T> withTag(ITag<T> tag) {
        return new TagOrEntry<>(tag);
    }

    public static <T extends IForgeRegistryEntry<T>> TagOrEntry<T> withEntry(T entry) {
        return new TagOrEntry<>(entry);
    }

    public T getFirst() {
        if (tag != null) {
            return tag.getValues().get(0);
        } else {
            assert entry != null;
            return entry;
        }
    }

    public boolean is(T t) {
        if (tag != null) {
            return tag.contains(t);
        } else {
            assert entry != null;
            return entry.equals(t);
        }
    }

    public boolean isTag() {
        return tag != null;
    }
}
