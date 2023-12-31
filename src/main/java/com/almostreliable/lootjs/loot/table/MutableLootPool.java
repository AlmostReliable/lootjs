package com.almostreliable.lootjs.loot.table;

import com.almostreliable.lootjs.LootJS;
import com.almostreliable.lootjs.core.entry.LootEntry;
import com.almostreliable.lootjs.core.entry.SimpleLootEntry;
import com.almostreliable.lootjs.loot.LootConditionList;
import com.almostreliable.lootjs.loot.LootEntryList;
import com.almostreliable.lootjs.loot.LootFunctionList;
import com.almostreliable.lootjs.loot.extension.LootPoolExtension;
import com.almostreliable.lootjs.util.DebugInfo;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Optional;
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

    public MutableLootPool() {
        vanillaPool = new LootPool(new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                ConstantValue.exactly(1),
                ConstantValue.exactly(0),
                Optional.empty());
    }

    public MutableLootPool rolls(NumberProvider rolls) {
        LootPoolExtension.cast(vanillaPool).lootjs$setRolls(rolls);
        return this;
    }

    public MutableLootPool bonusRolls(NumberProvider bonusRolls) {
        LootPoolExtension.cast(vanillaPool).lootjs$setBonusRolls(bonusRolls);
        return this;
    }

    public LootConditionList getConditions() {
        if (conditions == null) {
            conditions = LootPoolExtension.cast(vanillaPool).lootjs$createConditionList();
        }

        return conditions;
    }

    public LootFunctionList getFunctions() {
        if (functions == null) {
            functions = LootPoolExtension.cast(vanillaPool).lootjs$createFunctionList();
        }

        return functions;
    }

    public LootEntryList getEntries() {
        if (entries == null) {
            entries = LootPoolExtension.cast(vanillaPool).lootjs$createEntryList();
        }

        return entries;
    }

    public LootPool getVanillaPool() {
        return vanillaPool;
    }

    public void collectDebugInfo(DebugInfo info) {
        info.add("% Rolls -> " + getRollStr(LootPoolExtension.cast(vanillaPool).lootjs$getRolls()));
        info.add("% Bonus rolls -> " + getRollStr(LootPoolExtension.cast(vanillaPool).lootjs$getBonusRolls()));
        getEntries().collectDebugInfo(info);
        getConditions().collectDebugInfo(info);
        getFunctions().collectDebugInfo(info);
    }

    private String getRollStr(NumberProvider numberProvider) {
        JsonElement tree = NumberProviders.CODEC
                .encodeStart(JsonOps.INSTANCE, numberProvider)
                .getOrThrow(false, LootJS.LOG::error);
        if (numberProvider instanceof ConstantValue) {
            if (tree instanceof JsonObject json) {
                return json.get("value").getAsString();
            }

            return tree.getAsString();
        }

        if (numberProvider instanceof UniformGenerator) {
            if (tree instanceof JsonObject json) {
                return "[" + json.get("min").getAsString() + " -> " + json.get("max").getAsString() + "]";
            }

            return tree.getAsString();
        }

        // For now, we just do this if it's binomial
        return tree.toString();
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

    public MutableLootPool when(Consumer<LootConditionList> onConditions) {
        onConditions.accept(getConditions());
        return this;
    }

    public MutableLootPool apply(Consumer<LootFunctionList> onModifiers) {
        onModifiers.accept(getFunctions());
        return this;
    }
}
