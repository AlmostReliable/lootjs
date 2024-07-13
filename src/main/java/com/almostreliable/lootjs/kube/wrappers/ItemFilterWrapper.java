package com.almostreliable.lootjs.kube.wrappers;

import com.almostreliable.lootjs.core.filters.ItemFilter;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemFilterWrapper {

    private static ItemFilter ofItemFilterSingle(RegistryAccessContainer cx, @Nullable Object o) {
        if (o instanceof ItemFilter i) return i;

        if (o instanceof String str && !str.isEmpty()) {
            String first = str.substring(0, 1);
            String remaining = str.substring(1);
            switch (first) {
                case "*":
                    return ItemFilter.ANY;
                case "#":
                    ResourceLocation location = ResourceLocation.parse(remaining);
                    TagKey<Item> tag = TagKey.create(Registries.ITEM, location);
                    return new ItemFilter.Tag(tag);
                case "@":
                    return itemStack -> {
                        var key = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
                        return key.getNamespace().equals(remaining);
                    };
                case "!":
                    return ofItemFilterSingle(cx, remaining.trim()).negate();
            }
        }

        Ingredient ingredient = IngredientJS.wrap(cx, o);
        if (ingredient.isEmpty()) {
            return ItemFilter.EMPTY;
        }

        return new ItemFilter.Ingredient(ingredient);
    }

    public static ItemFilter ofItemFilter(RegistryAccessContainer cx, Object o) {
        if (o instanceof List<?> list) {
            List<ItemFilter> filters = new ArrayList<>(list.size());
            for (Object entry : list) {
                var filter = ofItemFilter(cx, entry);
                filters.add(filter);
            }

            return itemStack -> {
                for (ItemFilter filter : filters) {
                    if (filter.test(itemStack)) {
                        return true;
                    }
                }

                return false;
            };
        }

        return ofItemFilterSingle(cx, o);
    }
}
