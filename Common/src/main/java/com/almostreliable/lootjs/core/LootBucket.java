package com.almostreliable.lootjs.core;

import com.almostreliable.lootjs.core.entry.LootEntry;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.util.NullableFunction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;

import java.util.*;

public class LootBucket implements Iterable<ItemStack> {
    private final LootContext context;
    private final List<ItemStack> loot;
    private final ItemFilter filter;

    public LootBucket(LootContext context) {
        this(context, new ArrayList<>());
    }

    public LootBucket(LootContext context, List<ItemStack> loot) {
        this.context = context;
        this.loot = loot;
        this.filter = ItemFilter.ALWAYS_TRUE;
    }

    public LootBucket(LootContext context, List<ItemStack> loot, ItemFilter filter) {
        this.context = context;
        this.loot = loot;
        this.filter = filter;
    }

    public void addItem(ItemStack item) {
        if (filter.test(item)) {
            loot.add(item);
        }
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
        remove(ItemFilter.ALWAYS_TRUE);
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

    public void replace(ItemFilter filter, ItemStackFactory itemStackFactory) {
        replace(filter, itemStackFactory, false);
    }

    public void replace(ItemFilter filter, ItemStackFactory itemStackFactory, boolean preserveCount) {
        var it = iterator();
        while (it.hasNext()) {
            ItemStack thisItem = it.next();
            if (!filter.test(thisItem)) {
                continue;
            }

            ItemStack newItem = itemStackFactory.create(context);
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

    /**
     * Applies the provided item filter to the loot items.
     *
     * @param filter the filter to be applied on the loot items.
     * @return new LootItems instance with the applied filter.
     */
    public LootBucket filter(ItemFilter filter) {
        if (filter == ItemFilter.ALWAYS_TRUE) {
            return this;
        }

        return new LootBucket(context, loot, this.filter.and(filter));
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

        ItemStack itemStack = loot.get(index);
        if (filter.test(itemStack)) {
            return itemStack;
        }

        return ItemStack.EMPTY;
    }

    public int size() {
        int s = 0;
        for (ItemStack itemStack : this) {
            s++;
        }

        return s;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public It iterator() {
        return new It(loot.listIterator(), filter);
    }

    public It filteredIterator(ItemFilter filter) {
        return new It(loot.listIterator(), this.filter.and(filter));
    }

    public static class It implements Iterator<ItemStack> {
        private final ListIterator<ItemStack> iterator;
        private final ItemFilter filter;
        private ItemStack next;

        private It(ListIterator<ItemStack> iterator, ItemFilter filter) {
            this.iterator = iterator;
            this.filter = filter;
            advance();
        }

        private void advance() {
            while (iterator.hasNext()) {
                var itemStack = iterator.next();
                if (filter.test(itemStack)) {
                    next = itemStack;
                    return;
                }
            }

            next = null;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public ItemStack next() {
            if (next == null) throw new NoSuchElementException();
            var itemStack = next;
            advance();
            return itemStack;
        }

        public void set(ItemStack itemStack) {
            iterator.set(itemStack);
        }

        public void remove() {
            iterator.remove();
        }
    }
}
