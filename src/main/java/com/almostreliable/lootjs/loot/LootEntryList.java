package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.core.entry.CompositeLootEntry;
import com.almostreliable.lootjs.core.entry.LootEntry;
import com.almostreliable.lootjs.core.entry.SimpleLootEntry;
import com.almostreliable.lootjs.core.filters.ResourceLocationFilter;
import com.almostreliable.lootjs.loot.table.LootApplier;
import com.almostreliable.lootjs.util.DebugInfo;
import com.almostreliable.lootjs.util.ListHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class LootEntryList extends ListHolder<LootEntry, LootPoolEntryContainer> implements LootApplier {

    public LootEntryList() {
        super();
    }

    public LootEntryList(LootEntry... entries) {
        super(new ArrayList<>(entries.length));
        for (LootEntry entry : entries) {
            add(entry);
        }
    }

    public LootEntryList(List<LootPoolEntryContainer> entries) {
        super(entries);
    }

    @Override
    public ListIterator<LootEntry> iterator() {
        return new ListIt(elements);
    }

    @Override
    protected LootEntry wrap(LootPoolEntryContainer entry) {
        return LootEntry.ofVanilla(entry);
    }

    @Override
    protected LootPoolEntryContainer unwrap(LootEntry entry) {
        return entry.getVanillaEntry();
    }

    public List<LootPoolEntryContainer> createVanillaArray() {
        List<LootPoolEntryContainer> entries = new ArrayList<>(this.size());
        for (LootEntry e : this) {
            entries.add(e.getVanillaEntry());
        }

        return entries;
    }

    public void collectDebugInfo(DebugInfo info) {
        if (this.isEmpty()) return;

        info.add("% Entries: [");
        info.push();
        info.add("{");
        info.push();
        for (LootEntry entry : this) {
            entry.collectDebugInfo(info);
        }
        info.pop();
        info.add("}");
        info.pop();
        info.add("]");
    }

    public void transform(UnaryOperator<LootEntry> onTransform) {
        var it = iterator();
        while (it.hasNext()) {
            LootEntry entry = it.next();
            LootEntry transformed = onTransform.apply(entry);
            if (transformed == null) {
                it.remove();
                continue;
            }

            if (transformed != entry) {
                it.set(transformed);
            }
        }
    }

    public LootEntryList transformEntry(UnaryOperator<SimpleLootEntry> onTransform, boolean deepTransform) {
        transform(entry -> {
            if (entry instanceof LootApplier helper && deepTransform) {
                helper.transformEntry(onTransform, true);
                return entry;
            }

            if (entry instanceof SimpleLootEntry le) {
                return onTransform.apply(le);
            }

            return entry;
        });

        return this;
    }

    public LootEntryList removeEntry(Predicate<SimpleLootEntry> filter, boolean deepRemove) {
        var it = iterator();
        while (it.hasNext()) {
            LootEntry entry = it.next();
            if (entry instanceof CompositeLootEntry composite && deepRemove) {
                composite.removeEntry(filter, true);
                continue;
            }

            if (entry instanceof SimpleLootEntry le && filter.test(le)) {
                it.remove();
            }
        }

        return this;
    }

    @Override
    public LootEntryList addEntry(LootEntry entry) {
        add(entry);
        return this;
    }

    public boolean remove(ResourceLocationFilter type) {
        return elements.removeIf(element -> type.test(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE.getKey(element.getType())));
    }

    public boolean contains(LootPoolEntryType type) {
        return indexOf(type) != -1;
    }

    public int indexOf(LootPoolEntryType type) {
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).getType().equals(type)) {
                return i;
            }
        }

        return -1;
    }

    public int lastIndexOf(LootPoolEntryType type) {
        for (int i = elements.size() - 1; i >= 0; i--) {
            if (elements.get(i).getType().equals(type)) {
                return i;
            }
        }

        return -1;
    }

    private static class ListIt implements ListIterator<LootEntry> {

        private final ListIterator<LootPoolEntryContainer> it;

        public ListIt(List<LootPoolEntryContainer> entries) {
            this.it = entries.listIterator();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public LootEntry next() {
            return LootEntry.ofVanilla(it.next());
        }

        @Override
        public boolean hasPrevious() {
            return it.hasPrevious();
        }

        @Override
        public LootEntry previous() {
            return LootEntry.ofVanilla(it.previous());
        }

        @Override
        public int nextIndex() {
            return it.nextIndex();
        }

        @Override
        public int previousIndex() {
            return it.previousIndex();
        }

        @Override
        public void remove() {
            it.remove();
        }

        @Override
        public void set(LootEntry lootEntry) {
            it.set(lootEntry.getVanillaEntry());
        }

        @Override
        public void add(LootEntry lootEntry) {
            it.add(lootEntry.getVanillaEntry());
        }
    }
}
