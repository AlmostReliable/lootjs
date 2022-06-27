package com.almostreliable.lootjs.util;

import com.almostreliable.lootjs.LootJSPlatform;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;

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
                            "Property " + o + " does not exists for block " + LootJSPlatform.INSTANCE.getRegistryName(block));
                }
                propBuilder.hasProperty(property, value.get().toString());
            }
        }
        return propBuilder;
    }

    public static String formatEntity(Entity entity) {
        return String.format("Type=%s, Id=%s, Dim=%s, x=%.2f, y=%.2f, z=%.2f",
                quote(LootJSPlatform.INSTANCE.getRegistryName(entity.getType())),
                entity.getId(),
                quote(entity.level.dimension().location()),
                entity.getX(),
                entity.getY(),
                entity.getZ());
    }

    public static String formatPosition(Vec3 position) {
        return String.format("(%.2f, %.2f, %.2f)", position.x, position.y, position.z);
    }

    public static String formatItemStack(ItemStack itemStack) {
        String tag = "";
        ItemStack copy = itemStack.copy();
        if (copy.getTag() != null) {
            if (copy.getTag().contains("AttributeModifiers")) {
                copy.getTag().putString("AttributeModifiers", "...");
            }
            tag += " " + copy.getTag();
        }
        return copy + tag;
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
