package com.almostreliable.lootjs.loot.table;

import com.almostreliable.lootjs.core.entry.LootEntry;
import com.almostreliable.lootjs.core.entry.SimpleLootEntry;
import com.almostreliable.lootjs.loot.LootFunctionList;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class LootTableList implements LootApplier, Iterable<MutableLootTable> {

    private final List<MutableLootTable> tables;

    public LootTableList(List<MutableLootTable> tables) {
        this.tables = tables;
    }

    public List<MutableLootTable> getTables() {
        return tables;
    }

    public LootTableList createPool(Consumer<MutableLootPool> onCreatePool) {
        getTables().forEach(table -> table.createPool(onCreatePool));
        return this;
    }

    public LootTableList firstPool(Consumer<MutableLootPool> onModifyPool) {
        getTables().forEach(table -> table.firstPool(onModifyPool));
        return this;
    }

    public LootTableList onDrop(PostLootAction postLootAction) {
        getTables().forEach(table -> table.onDrop(postLootAction));
        return this;
    }

    public LootTableList clear() {
        getTables().forEach(MutableLootTable::clear);
        return this;
    }

    public LootTableList apply(Consumer<LootFunctionList> onModifiers) {
        getTables().forEach(table -> table.apply(onModifiers));
        return this;
    }

    @Override
    public LootTableList addEntry(LootEntry entry) {
        getTables().forEach(table -> table.addEntry(entry));
        return this;
    }

    @Override
    public LootTableList transformEntry(UnaryOperator<SimpleLootEntry> onTransform, boolean deepTransform) {
        getTables().forEach(table -> table.transformEntry(onTransform, deepTransform));
        return this;
    }

    @Override
    public LootTableList removeEntry(Predicate<SimpleLootEntry> onRemove, boolean deepRemove) {
        getTables().forEach(table -> table.removeEntry(onRemove, deepRemove));
        return this;
    }

    @NotNull
    @Override
    public Iterator<MutableLootTable> iterator() {
        return getTables().iterator();
    }
}
