package com.almostreliable.lootjs.loot.table;

import com.almostreliable.lootjs.core.entry.*;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.core.filters.ResourceLocationFilter;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public interface LootTransformHelper {

    void transformEntry(UnaryOperator<SimpleLootEntry> onTransform, boolean deepTransform);

    default void transformEntry(UnaryOperator<SimpleLootEntry> onTransform) {
        transformEntry(onTransform, true);
    }

    void removeEntry(Predicate<SimpleLootEntry> onRemove, boolean deepRemove);

    default void removeEntry(Predicate<SimpleLootEntry> onRemove) {
        removeEntry(onRemove, true);
    }

    default void removeItem(ItemFilter filter) {
        removeItem(filter, true);
    }

    default void removeItem(ItemFilter filter, boolean deepRemove) {
        removeEntry(entry -> entry instanceof ItemLootEntry ile && ile.test(filter), deepRemove);
    }

    default void removeTag(String tag) {
        removeTag(tag, true);
    }

    default void removeTag(String tag, boolean deepRemove) {
        removeEntry(entry -> entry instanceof TagLootEntry tle && tle.isTag(tag), deepRemove);
    }

    default void removeReference(ResourceLocationFilter filter) {
        removeReference(filter, true);
    }

    default void removeReference(ResourceLocationFilter filter, boolean deepRemove) {
        removeEntry(entry -> entry instanceof TableReferenceLootEntry d && filter.test(d.getLocation()), deepRemove);
    }

    default void removeDynamic(ResourceLocationFilter filter) {
        removeDynamic(filter, true);
    }

    default void removeDynamic(ResourceLocationFilter filter, boolean deepRemove) {
        removeEntry(entry -> entry instanceof DynamicLootEntry d && filter.test(d.getLocation()), deepRemove);
    }

    default void replaceItem(ItemFilter filter, ItemStack item) {
        replaceItem(filter, item, true);
    }

    default void replaceItem(ItemFilter filter, ItemStack item, boolean deepReplace) {
        transformEntry(entry -> {
            if (entry instanceof ItemLootEntry ile && ile.test(filter)) {
                return new ItemLootEntry(item);
            }

            return entry;
        }, deepReplace);
    }
}
