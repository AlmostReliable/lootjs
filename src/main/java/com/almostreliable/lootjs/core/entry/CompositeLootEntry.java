package com.almostreliable.lootjs.core.entry;

import com.almostreliable.lootjs.loot.LootConditionList;
import com.almostreliable.lootjs.loot.LootEntryList;
import com.almostreliable.lootjs.loot.extension.CompositeEntryBaseExtension;
import com.almostreliable.lootjs.loot.table.LootAppendHelper;
import com.almostreliable.lootjs.loot.table.LootTransformHelper;
import com.almostreliable.lootjs.util.DebugInfo;
import com.almostreliable.lootjs.util.NullableFunction;
import net.minecraft.world.level.storage.loot.entries.CompositeEntryBase;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CompositeLootEntry implements LootEntry, LootTransformHelper, LootAppendHelper {
    private final CompositeEntryBase vanillaEntry;
    @Nullable
    private LootEntryList entries;
    @Nullable
    protected LootConditionList conditions;

    public CompositeLootEntry(CompositeEntryBase vanillaEntry) {
        this.vanillaEntry = vanillaEntry;
    }

    public LootEntryList getEntries() {
        if (entries == null) {
            entries = new LootEntryList(((CompositeEntryBaseExtension) vanillaEntry).lootjs$getEntries());
        }

        return entries;
    }

    public CompositeLootEntry entries(Consumer<LootEntryList> callback) {
        callback.accept(getEntries());
        return this;
    }

    @Override
    public LootPoolEntryType getVanillaType() {
        return vanillaEntry.getType();
    }

    @Override
    public CompositeEntryBase getVanillaEntry() {
        free();
        return vanillaEntry;
    }

    @Override
    public CompositeLootEntry when(Consumer<LootConditionList> callback) {
        callback.accept(getConditions());
        return this;
    }

    @Override
    public LootConditionList getConditions() {
        if (conditions == null) {
            conditions = new LootConditionList(vanillaEntry.conditions);
        }

        return conditions;
    }

    @Override
    public void collectDebugInfo(DebugInfo info) {
        info.add(getType().toString());
        info.push();
        entries(entries -> entries.collectDebugInfo(info));
        when(conditions -> conditions.collectDebugInfo(info));
        info.pop();
    }

    protected void free() {
        if (conditions != null) {
            vanillaEntry.conditions = conditions;
            // TODO: let LootCOnditionList implement composite check?
            vanillaEntry.compositeCondition = LootItemConditions.andConditions(vanillaEntry.conditions);
        }

        if (entries != null) {
            ((CompositeEntryBaseExtension) vanillaEntry).lootjs$setEntries(entries.createVanillaArray());
            entries = null;
        }
    }

    @Override
    public void addEntry(LootEntry entry) {
        entries(entries -> entries.add(entry));
    }

    @Override
    public void transformEntry(NullableFunction<SimpleLootEntry, Object> onTransform, boolean deepTransform) {
        getEntries().transformEntry(onTransform, deepTransform);
    }

    @Override
    public void removeEntry(Predicate<SimpleLootEntry> onRemove, boolean deepRemove) {
        getEntries().removeEntry(onRemove, deepRemove);
    }
}
