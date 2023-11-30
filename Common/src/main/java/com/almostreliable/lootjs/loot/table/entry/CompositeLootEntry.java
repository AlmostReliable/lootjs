package com.almostreliable.lootjs.loot.table.entry;

import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.loot.extension.CompositeEntryBaseExtension;
import com.almostreliable.lootjs.loot.LootConditionList;
import com.almostreliable.lootjs.loot.LootEntryList;
import com.almostreliable.lootjs.util.DebugInfo;
import com.almostreliable.lootjs.util.NullableFunction;
import net.minecraft.world.level.storage.loot.entries.*;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CompositeLootEntry implements LootContainer, LootTransformHelper, LootAppendHelper {
    @Nullable
    private LootEntryList entries;
    @Nullable private LootConditionList conditions;
    protected final CompositeEntryBase origin;

    public static CompositeLootEntry alternative() {
        var entry = new AlternativesEntry(new LootPoolEntryContainer[0], new LootItemCondition[0]);
        return new CompositeLootEntry(entry);
    }

    public static CompositeLootEntry sequence() {
        var entry = new SequentialEntry(new LootPoolEntryContainer[0], new LootItemCondition[0]);
        return new CompositeLootEntry(entry);
    }

    public static CompositeLootEntry group() {
        var entry = new EntryGroup(new LootPoolEntryContainer[0], new LootItemCondition[0]);
        return new CompositeLootEntry(entry);
    }

    public CompositeLootEntry(CompositeEntryBase origin) {
        this.origin = origin;
    }

    public LootEntryList getEntries() {
        if (entries == null) {
            entries = new LootEntryList(((CompositeEntryBaseExtension) origin).lootjs$getEntries());
        }

        return entries;
    }

    public CompositeLootEntry entries(Consumer<LootEntryList> callback) {
        callback.accept(getEntries());
        return this;
    }

    @Override
    public CompositeLootEntry when(Consumer<LootConditionList> callback) {
        if (conditions == null) {
            conditions = new LootConditionList(origin.conditions);
        }

        callback.accept(conditions);
        return this;
    }

    @Override
    public boolean test(ItemFilter filter) {
        var entries = getEntries();

        if (entries.isEmpty()) {
            return filter == ItemFilter.EMPTY;
        }

        for (LootContainer entry : entries) {
            if (!entry.test(filter)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void collectDebugInfo(DebugInfo info) {
        info.add(getType().toString());
        info.push();
        entries(entries -> entries.collectDebugInfo(info));
        when(conditions -> conditions.collectDebugInfo(info));
        info.pop();
    }

    @Override
    public boolean isComposite() {
        return true;
    }

    @Override
    public LootPoolEntryContainer saveAndGetOrigin() {
        if (conditions != null) {
            origin.conditions = conditions.createVanillaArray();
            origin.compositeCondition = LootItemConditions.andConditions(origin.conditions);
        }

        if (entries != null) {
            ((CompositeEntryBaseExtension) origin).lootjs$setEntries(entries.createVanillaArray());
        }

        return origin;
    }

    @Override
    public LootPoolEntryType getVanillaType() {
        return origin.getType();
    }

    @Override
    public void addEntry(LootContainer entry) {
        entries(entries -> entries.add(entry));
    }

    @Override
    public void transformLoot(NullableFunction<LootEntry, Object> onTransform, boolean deepTransform) {
        getEntries().transformLoot(onTransform, deepTransform);
    }

    @Override
    public void removeLoot(Predicate<LootEntry> onRemove, boolean deepRemove) {
        getEntries().removeLoot(onRemove, deepRemove);
    }
}
