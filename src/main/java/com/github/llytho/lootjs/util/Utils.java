package com.github.llytho.lootjs.util;

import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.Property;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollection;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Utils {

    public static <T> String getClassNameEnding(T t) {
        String tName = t.getClass().getName();
        return tName.substring(tName.lastIndexOf('.') + 1);
    }

    public static StatePropertiesPredicate createProperties(Block block, Map<String, String> propertyMap) {
        StatePropertiesPredicate.Builder propBuilder = StatePropertiesPredicate.Builder.properties();
        if (propertyMap.isEmpty()) return StatePropertiesPredicate.ANY;

        Collection<Property<?>> properties = block.defaultBlockState().getProperties();
        for (Property<?> property : properties) {
            Object o = propertyMap.remove(property.getName());
            if (o != null) {
                Optional<?> value = property.getValue(o.toString());
                if (!value.isPresent()) {
                    throw new IllegalArgumentException(
                            "Property " + o + " does not exists for block " + block.getRegistryName());
                }
                propBuilder.hasProperty(property, value.get().toString());
            }
        }
        return propBuilder.build();
    }

    public static <T extends IForgeRegistryEntry<T>> TagOrEntry<T> getTagOrEntry(IForgeRegistry<T> registry, String idOrTag) {
        @SuppressWarnings("unchecked")
        ITagCollection<T> tagCollection = (ITagCollection<T>) getTagCollectionByRegistry(registry);
        if (idOrTag.startsWith("#")) {
            ITag<T> tag = tagCollection.getTag(new ResourceLocation(idOrTag.substring(1)));
            if (tag == null) {
                throw new IllegalArgumentException(
                        "Tag " + idOrTag + " does not exists for " + registry.getRegistryName());
            }
            return TagOrEntry.withTag(tag);
        } else {
            T entry = registry.getValue(new ResourceLocation(idOrTag));
            if (entry == null || entry.getRegistryName() == null ||
                entry.getRegistryName().equals(registry.getDefaultKey())) {
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

    @Nullable
    public static String formatEntity(@Nullable Entity entity) {
        if (entity == null) {
            return null;
        }

        return String.format("Type=%s, Id=%s, Dim=%s, x=%.2f, y=%.2f, z=%.2f",
                quote(entity.getType().getRegistryName()),
                entity.getId(),
                entity.level == null ? "~NO DIM~" : quote(entity.level.dimension().location()),
                entity.getX(),
                entity.getY(),
                entity.getZ());
    }

    @Nullable
    public static String formatPosition(@Nullable Vector3d position) {
        if (position == null) {
            return null;
        }

        return String.format("(%.2f, %.2f, %.2f)", position.x, position.y, position.z);
    }

    @Nullable
    public static String formatItemStack(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return null;
        }

        String tag = "";
        if (itemStack.hasTag()) tag += " " + itemStack.getTag();
        return itemStack + tag;
    }

    public static String quote(String s) {
        return "\"" + s + "\"";
    }

    public static String quote(@Nullable ResourceLocation rl) {
        return quote(rl == null ? "NO_LOCATION" : rl.toString());
    }

    public static String quote(String prefix, Object[] objects) {
        return prefix + "[" +
               Arrays.stream(objects).map(Object::toString).map(Utils::quote).collect(Collectors.joining(",")) + "]";
    }
}
