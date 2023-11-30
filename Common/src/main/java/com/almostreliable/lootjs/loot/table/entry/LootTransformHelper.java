package com.almostreliable.lootjs.loot.table.entry;

import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.core.filters.ResourceLocationFilter;
import com.almostreliable.lootjs.util.NullableFunction;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public interface LootTransformHelper {

    void transformLoot(NullableFunction<LootEntry, Object> onTransform, boolean deepTransform);

    default void transformLoot(NullableFunction<LootEntry, Object> onTransform) {
        transformLoot(onTransform, true);
    }

    void removeLoot(Predicate<LootEntry> onRemove, boolean deepRemove);

    default void removeLoot(Predicate<LootEntry> onRemove) {
        removeLoot(onRemove, true);
    }

    default void removeItem(ItemFilter filter) {
        removeItem(filter, true);
    }

    default void removeItem(ItemFilter filter, boolean deepRemove) {
        removeLoot(entry -> entry.isItem() && entry.test(filter), deepRemove);
    }

    default void removeTag(String tag) {
        removeTag(tag, true);
    }

    default void removeTag(String tag, boolean deepRemove) {
        removeLoot(entry -> entry.isTag() && entry.testTag(tag), deepRemove);
    }

    default void removeReference(ResourceLocationFilter filter) {
        removeReference(filter, true);
    }

    default void removeReference(ResourceLocationFilter filter, boolean deepRemove) {
        removeLoot(entry -> entry.isReference() && filter.test(entry.getReference()), deepRemove);
    }

    default void removeDynamic(ResourceLocationFilter filter) {
        removeDynamic(filter, true);
    }

    default void removeDynamic(ResourceLocationFilter filter, boolean deepRemove) {
        removeLoot(entry -> entry.isDynamic() && filter.test(entry.getDynamic()), deepRemove);
    }

    default void replaceItem(ItemFilter filter, ItemStack item) {
        replaceItem(filter, item, true);
    }

    default void replaceItem(ItemFilter filter, ItemStack item, boolean deepReplace) {
        transformLoot(entry -> {
            if (entry.test(filter)) {
                return item;
            }

            return entry;
        }, deepReplace);
    }
}
