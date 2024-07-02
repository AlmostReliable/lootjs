package com.almostreliable.lootjs.core;

import com.almostreliable.lootjs.core.entry.ItemLootEntry;
import com.almostreliable.lootjs.core.entry.LootEntry;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.util.NullableFunction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class LootBucket implements Iterable<ItemStack> {
    private final LootContext context;
    private final List<ItemStack> loot;

    public LootBucket(LootContext context) {
        this(context, new ArrayList<>());
    }

    public LootBucket(LootContext context, List<ItemStack> loot) {
        this.context = context;
        this.loot = loot;
    }

    public void addItem(ItemStack item) {
        loot.add(item);
    }

    public void addAllItems(List<ItemStack> items) {
        for (ItemStack item : items) {
            addItem(item);
        }
    }

    public void addEntry(LootEntry unknown) {
        unknown.getVanillaEntry().expand(context, this::addEntry);
    }

    private void addEntry(LootPoolEntry entry) {
        entry.createItemStack(this::addItem, context);
    }

    public void remove(ItemFilter removeFilter) {
        var it = iterator();
        while (it.hasNext()) {
            var itemStack = it.next();
            if (removeFilter.test(itemStack)) {
                it.remove();
            }
        }
    }

    public void clear() {
        remove(ItemFilter.ANY);
    }

    public boolean hasItem(ItemFilter filter) {
        for (ItemStack itemStack : this) {
            if (filter.test(itemStack)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Modifies the loot items using the provided function. If the function returns null, the item is removed.
     *
     * @param function the function to be applied on the loot items.
     */
    public void modifyItems(NullableFunction<ItemStack, ItemStack> function) {
        var it = iterator();
        while (it.hasNext()) {
            var result = function.apply(it.next());
            if (result == null) {
                it.remove();
                continue;
            }

            it.set(result);
        }
    }

    public void replace(ItemFilter filter, ItemLootEntry itemLootEntry) {
        replace(filter, itemLootEntry, false);
    }

    public void replace(ItemFilter filter, ItemLootEntry itemLootEntry, boolean preserveCount) {
        var it = iterator();
        while (it.hasNext()) {
            ItemStack thisItem = it.next();
            if (!filter.test(thisItem)) {
                continue;
            }

            ItemStack newItem = itemLootEntry.create(context);
            if (newItem == null) {
                continue;
            }

            if (newItem.isEmpty()) {
                it.remove();
                continue;
            }

            if (preserveCount) {
                newItem.setCount(Math.min(thisItem.getCount(), newItem.getMaxStackSize()));
            }

            it.set(newItem);
        }
    }

    public LootBucket extract(ItemFilter filter) {
        List<ItemStack> extracted = new ArrayList<>();
        var it = iterator();
        while (it.hasNext()) {
            var itemStack = it.next();
            if (filter.test(itemStack)) {
                extracted.add(itemStack);
                it.remove();
            }
        }

        return new LootBucket(context, extracted);
    }

    public void merge(LootBucket other) {
        addAllItems(other.loot);
    }

    /**
     * Returns the item stack at the given index. If the index is out of bounds or if the item stack does not pass the filter, an empty item stack is returned.
     *
     * @param index the index.
     * @return the item stack.
     */
    public ItemStack get(int index) {
        if (index < 0 || index >= loot.size()) {
            return ItemStack.EMPTY;
        }

        return loot.get(index);
    }

    public int size() {
        return loot.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public ListIterator<ItemStack> iterator() {
        return loot.listIterator();
    }
}
