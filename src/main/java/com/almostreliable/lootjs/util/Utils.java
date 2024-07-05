package com.almostreliable.lootjs.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object o) {
        return (T) o;
    }

    public static <T> String getClassNameEnding(T t) {
        String tName = t.getClass().getName();
        return tName.substring(tName.lastIndexOf('.') + 1);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> mapOrThrow(Object o) {
        if (o instanceof Map<?, ?> m) {
            return (Map<String, Object>) m;
        }

        throw new RuntimeException("Expected map, got " + o.getClass().getName());
    }

    @SuppressWarnings("unchecked")
    public static List<Object> listOrThrow(Object o) {
        if (o instanceof List<?> l) {
            return (List<Object>) l;
        }

        throw new RuntimeException("Expected list, got " + o.getClass().getName());
    }

    public static String formatEntity(Entity entity) {
        return String.format("Type=%s, Id=%s, Dim=%s, x=%.2f, y=%.2f, z=%.2f",
                quote(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType())),
                entity.getId(),
                quote(entity.level().dimension().location()),
                entity.getX(),
                entity.getY(),
                entity.getZ());
    }

    public static String formatPosition(Vec3 position) {
        return String.format("(%.2f, %.2f, %.2f)", position.x, position.y, position.z);
    }

    public static String formatItemStack(ItemStack itemStack) {
        return itemStack.toString();
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
