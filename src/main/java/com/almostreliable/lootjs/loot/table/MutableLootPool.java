package com.almostreliable.lootjs.loot.table;

import com.almostreliable.lootjs.core.entry.LootEntry;
import com.almostreliable.lootjs.core.entry.SimpleLootEntry;
import com.almostreliable.lootjs.loot.LootConditionList;
import com.almostreliable.lootjs.loot.LootEntryList;
import com.almostreliable.lootjs.loot.LootFunctionList;
import com.almostreliable.lootjs.loot.extension.LootPoolExtension;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class MutableLootPool implements LootApplier {

    @Nullable private LootConditionList conditions;
    @Nullable private LootFunctionList functions;
    @Nullable private LootEntryList entries;
    private final LootPool vanillaPool;

    public MutableLootPool(LootPool pool) {
        vanillaPool = pool;
    }

    @Nullable
    public String getName() {
        return vanillaPool.getName();
    }

    public MutableLootPool name(String name) {
        LootPoolExtension.cast(vanillaPool).lootjs$setName(name);
        return this;
    }

    public MutableLootPool rolls(NumberProvider rolls) {
        vanillaPool.setRolls(rolls);
        return this;
    }

    public MutableLootPool bonusRolls(NumberProvider bonusRolls) {
        vanillaPool.setBonusRolls(bonusRolls);
        return this;
    }

    public MutableLootPool when(Consumer<LootConditionList> onConditions) {
        onConditions.accept(getConditions());
        return this;
    }

    public MutableLootPool apply(Consumer<LootFunctionList> onModifiers) {
        onModifiers.accept(getFunctions());
        return this;
    }

    public LootConditionList getConditions() {
        if (conditions == null) {
            conditions = LootPoolExtension.cast(vanillaPool).lootjs$getConditions();
        }

        return conditions;
    }

    public LootFunctionList getFunctions() {
        if (functions == null) {
            functions = LootPoolExtension.cast(vanillaPool).lootjs$getFunctions();
        }

        return functions;
    }

    public LootEntryList getEntries() {
        if (entries == null) {
            entries = LootPoolExtension.cast(vanillaPool).lootjs$getEntries();
        }

        return entries;
    }

    public LootPool getVanillaPool() {
        return vanillaPool;
    }

    @Override
    public MutableLootPool addEntry(LootEntry entry) {
        getEntries().add(entry);
        return this;
    }

    @Override
    public MutableLootPool transformEntry(UnaryOperator<SimpleLootEntry> onTransform, boolean deepTransform) {
        getEntries().transformEntry(onTransform, deepTransform);
        return this;
    }

    @Override
    public MutableLootPool removeEntry(Predicate<SimpleLootEntry> onRemove, boolean deepRemove) {
        getEntries().removeEntry(onRemove, deepRemove);
        return this;
    }
}
