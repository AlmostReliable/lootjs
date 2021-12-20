package com.github.llytho.lootjs.util;

import net.minecraft.tags.ITag;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class TagOrEntry<T extends IForgeRegistryEntry<T>> {
    public ITag<T> tag;
    public T entry;

    TagOrEntry() {}

    public static <T extends IForgeRegistryEntry<T>> TagOrEntry<T> withTag(ITag<T> tag) {
        TagOrEntry<T> te = new TagOrEntry<>();
        te.tag = tag;
        return te;
    }

    public static <T extends IForgeRegistryEntry<T>> TagOrEntry<T> withEntry(T entry) {
        TagOrEntry<T> te = new TagOrEntry<>();
        te.entry = entry;
        return te;
    }

    public boolean isTag() {
        return tag != null;
    }
}
