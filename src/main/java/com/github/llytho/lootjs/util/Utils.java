package com.github.llytho.lootjs.util;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.SerializationTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Utils {

    public static <T> String getClassNameEnding(T t) {
        String tName = t.getClass().getName();
        return tName.substring(tName.lastIndexOf('.') + 1);
    }

    public static StatePropertiesPredicate.Builder createProperties(Block block, Map<String, String> propertyMap) {
        StatePropertiesPredicate.Builder propBuilder = StatePropertiesPredicate.Builder.properties();
        if (propertyMap.isEmpty()) return propBuilder;

        Collection<Property<?>> properties = block.defaultBlockState().getProperties();
        for (Property<?> property : properties) {
            Object o = propertyMap.remove(property.getName());
            if (o != null) {
                Optional<?> value = property.getValue(o.toString());
                if (value.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Property " + o + " does not exists for block " + block.getRegistryName());
                }
                propBuilder.hasProperty(property, value.get().toString());
            }
        }
        return propBuilder;
    }

    public static <T extends IForgeRegistryEntry<T>> TagOrEntry<T> getTagOrEntry(IForgeRegistry<T> registry, String idOrTag) {
        @SuppressWarnings("unchecked")
        TagCollection<T> tagCollection = (TagCollection<T>) getTagCollectionByRegistry(registry);
        if (idOrTag.startsWith("#")) {
            Tag<T> tag = tagCollection.getTag(new ResourceLocation(idOrTag.substring(1)));
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

    public static TagCollection<? extends IForgeRegistryEntry<?>> getTagCollectionByRegistry(IForgeRegistry<?> registry) {
        if (registry == ForgeRegistries.BLOCKS) {
            return SerializationTags.getInstance().getOrEmpty(Registry.BLOCK_REGISTRY);
        }

        if (registry == ForgeRegistries.FLUIDS) {
            return SerializationTags.getInstance().getOrEmpty(Registry.FLUID_REGISTRY);
        }

        if (registry == ForgeRegistries.ITEMS) {
            return SerializationTags.getInstance().getOrEmpty(Registry.ITEM_REGISTRY);
        }

        if (registry == ForgeRegistries.ENTITIES) {
            return SerializationTags.getInstance().getOrEmpty(Registry.ENTITY_TYPE_REGISTRY);
        }

        throw new IllegalArgumentException(registry.getRegistryName() + " does not provide tags");
    }

    @Nullable
    public static String formatEntity(@Nullable Entity entity) {
        if (entity == null) {
            return null;
        }

        return String.format("Type=%s, Id=%s, Dim=%s, x=%.2f, y=%.2f, z=%.2f",
                quote(entity.getType().getRegistryName()),
                entity.getId(),
                quote(entity.level.dimension().location()),
                entity.getX(),
                entity.getY(),
                entity.getZ());
    }

    @Nullable
    public static String formatPosition(@Nullable Vec3 position) {
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

    public static String quote(String prefix, Collection<?> objects) {
        return prefix + "[" +
               objects.stream().map(Object::toString).map(Utils::quote).collect(Collectors.joining(",")) + "]";
    }
}
