package com.github.llytho.lootjs.util;

import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollection;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class Utils {
    public static <T extends IForgeRegistryEntry<T>> TagOrEntry<T> getTagOrEntry(IForgeRegistry<T> registry, String idOrTag) {
        @SuppressWarnings("unchecked") ITagCollection<T> tagCollection = (ITagCollection<T>) getTagCollectionByRegistry(
                registry);
        if (idOrTag.startsWith("#")) {
            ITag<T> tag = tagCollection.getTag(new ResourceLocation(idOrTag.substring(1)));
            if (tag == null) {
                throw new IllegalArgumentException(
                        "Tag " + idOrTag + " does not exists for " + registry.getRegistryName());
            }
            return TagOrEntry.withTag(tag);
        } else {
            T entry = registry.getValue(new ResourceLocation(idOrTag));
            if (entry == null) {
                throw new IllegalArgumentException(
                        "Type " + idOrTag + " does not exists for " + registry.getRegistryName());
            }
            return TagOrEntry.withEntry(entry);
        }
    }

    public static ITagCollection<? extends IForgeRegistryEntry<?>> getTagCollectionByRegistry(IForgeRegistry<?> registry) {
        if (registry == ForgeRegistries.BLOCKS) {
            return TagCollectionManager.getInstance().getBlocks();
        }

        if (registry == ForgeRegistries.FLUIDS) {
            return TagCollectionManager.getInstance().getFluids();
        }

        if (registry == ForgeRegistries.ITEMS) {
            return TagCollectionManager.getInstance().getItems();
        }

        if (registry == ForgeRegistries.ENTITIES) {
            return TagCollectionManager.getInstance().getEntityTypes();
        }

        return TagCollectionManager.getInstance().getCustomTypeCollection(registry);
    }

    public static class TagOrEntry<T extends IForgeRegistryEntry<T>> {
        public ITag<T> tag;
        public T entry;

        private TagOrEntry() {}

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
}
