package com.almostreliable.lootjs.loot.table;

import com.almostreliable.lootjs.core.entry.ItemLootEntry;
import com.almostreliable.lootjs.core.entry.SimpleLootEntry;
import com.almostreliable.lootjs.core.entry.TableReferenceLootEntry;
import com.almostreliable.lootjs.core.entry.TagLootEntry;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.core.filters.ResourceLocationFilter;
import net.minecraft.world.item.Item;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@SuppressWarnings("UnusedReturnValue")
public interface LootEntriesTransformer {

    LootEntriesTransformer modifyEntry(UnaryOperator<SimpleLootEntry> onTransform, boolean deepTransform);

    default LootEntriesTransformer modifyEntry(UnaryOperator<SimpleLootEntry> onTransform) {
        modifyEntry(onTransform, true);
        return this;
    }

    default LootEntriesTransformer modifyItem(UnaryOperator<ItemLootEntry> onTransform) {
        modifyEntry(le -> {
            if (le instanceof ItemLootEntry ile) {
                return onTransform.apply(ile);
            }

            return le;
        });

        return this;
    }

    LootEntriesTransformer removeEntry(Predicate<SimpleLootEntry> onRemove, boolean deepRemove);

    default LootEntriesTransformer removeEntry(Predicate<SimpleLootEntry> onRemove) {
        removeEntry(onRemove, true);
        return this;
    }

    default LootEntriesTransformer removeItem(ItemFilter filter) {
        removeItem(filter, true);
        return this;
    }

    default LootEntriesTransformer removeItem(ItemFilter filter, boolean deepRemove) {
        removeEntry(entry -> entry instanceof ItemLootEntry ile && ile.test(filter), deepRemove);
        return this;
    }

    default LootEntriesTransformer removeTag(String tag) {
        removeTag(tag, true);
        return this;
    }

    default LootEntriesTransformer removeTag(String tag, boolean deepRemove) {
        removeEntry(entry -> entry instanceof TagLootEntry tle && tle.isTag(tag), deepRemove);
        return this;
    }

    default LootEntriesTransformer removeReference(ResourceLocationFilter filter) {
        removeReference(filter, true);
        return this;
    }

    default LootEntriesTransformer removeReference(ResourceLocationFilter filter, boolean deepRemove) {
        removeEntry(entry -> entry instanceof TableReferenceLootEntry d && filter.test(d.getLocation()), deepRemove);
        return this;
    }

    default LootEntriesTransformer replaceItem(ItemFilter filter, Item item) {
        replaceItem(filter, item, true);
        return this;
    }

    default LootEntriesTransformer replaceItem(ItemFilter filter, Item item, boolean deepReplace) {
        modifyEntry(entry -> {
            if (entry instanceof ItemLootEntry ile && ile.test(filter)) {
                ile.setItem(item);
            }

            return entry;
        }, deepReplace);
        return this;
    }
}
