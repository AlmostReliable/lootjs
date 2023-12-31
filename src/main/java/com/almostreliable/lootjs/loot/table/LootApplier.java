package com.almostreliable.lootjs.loot.table;

import com.almostreliable.lootjs.LootJS;
import com.almostreliable.lootjs.core.entry.*;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.core.filters.ResourceLocationFilter;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@SuppressWarnings("UnusedReturnValue")
public interface LootApplier {
    default LootApplier addCustomEntry(JsonObject json) {
        var vanilla = LootPoolEntries.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(false, LootJS.LOG::error);
        return addEntry(LootEntry.ofVanilla(vanilla));
    }

    default LootApplier addItem(ItemStack itemStack) {
        return addEntry(LootEntry.of(itemStack));
    }

    LootApplier addEntry(LootEntry entry);

    LootApplier transformEntry(UnaryOperator<SimpleLootEntry> onTransform, boolean deepTransform);

    default LootApplier transformEntry(UnaryOperator<SimpleLootEntry> onTransform) {
        transformEntry(onTransform, true);
        return this;
    }

    LootApplier removeEntry(Predicate<SimpleLootEntry> onRemove, boolean deepRemove);

    default LootApplier removeEntry(Predicate<SimpleLootEntry> onRemove) {
        removeEntry(onRemove, true);
        return this;
    }

    default LootApplier removeItem(ItemFilter filter) {
        removeItem(filter, true);
        return this;
    }

    default LootApplier removeItem(ItemFilter filter, boolean deepRemove) {
        removeEntry(entry -> entry instanceof ItemLootEntry ile && ile.test(filter), deepRemove);
        return this;
    }

    default LootApplier removeTag(String tag) {
        removeTag(tag, true);
        return this;
    }

    default LootApplier removeTag(String tag, boolean deepRemove) {
        removeEntry(entry -> entry instanceof TagLootEntry tle && tle.isTag(tag), deepRemove);
        return this;
    }

    default LootApplier removeReference(ResourceLocationFilter filter) {
        removeReference(filter, true);
        return this;
    }

    default LootApplier removeReference(ResourceLocationFilter filter, boolean deepRemove) {
        removeEntry(entry -> entry instanceof TableReferenceLootEntry d && filter.test(d.getLocation()), deepRemove);
        return this;
    }

    default LootApplier removeDynamic(ResourceLocationFilter filter) {
        removeDynamic(filter, true);
        return this;
    }

    default LootApplier removeDynamic(ResourceLocationFilter filter, boolean deepRemove) {
        removeEntry(entry -> entry instanceof DynamicLootEntry d && filter.test(d.getLocation()), deepRemove);
        return this;
    }

    default LootApplier replaceItem(ItemFilter filter, ItemStack item) {
        replaceItem(filter, item, true);
        return this;
    }

    default LootApplier replaceItem(ItemFilter filter, ItemStack item, boolean deepReplace) {
        transformEntry(entry -> {
            if (entry instanceof ItemLootEntry ile && ile.test(filter)) {
                return new ItemLootEntry(item);
            }

            return entry;
        }, deepReplace);
        return this;
    }
}
