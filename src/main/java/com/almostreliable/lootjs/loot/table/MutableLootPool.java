package com.almostreliable.lootjs.loot.table;

import com.almostreliable.lootjs.LootJS;
import com.almostreliable.lootjs.core.entry.LootEntry;
import com.almostreliable.lootjs.core.entry.SimpleLootEntry;
import com.almostreliable.lootjs.loot.LootConditionList;
import com.almostreliable.lootjs.loot.LootEntryList;
import com.almostreliable.lootjs.loot.LootFunctionList;
import com.almostreliable.lootjs.loot.extension.LootPoolExtension;
import com.almostreliable.lootjs.util.DebugInfo;
import com.almostreliable.lootjs.util.NullableFunction;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class MutableLootPool implements LootTransformHelper, LootAppendHelper {

    private final LootConditionList conditions;
    private final LootFunctionList functions;
    private final LootEntryList entries;
    private NumberProvider rolls = ConstantValue.exactly(1);
    private NumberProvider bonusRolls = ConstantValue.exactly(0);
    @Nullable private LootPool vanillaPool;

    public MutableLootPool(LootPool pool) {
        var accessor = (LootPoolExtension) pool;
        vanillaPool = pool;
        conditions = new LootConditionList(accessor.lootjs$getConditions());
        functions = new LootFunctionList(accessor.lootjs$getFunctions());
        entries = new LootEntryList(accessor.lootjs$getEntries()); // TODO
        rolls = accessor.lootjs$getRolls();
        bonusRolls = accessor.lootjs$getBonusRolls();
    }

    public MutableLootPool() {
        conditions = new LootConditionList();
        functions = new LootFunctionList();
        entries = new LootEntryList();
    }

    public MutableLootPool rolls(NumberProvider rolls) {
        this.rolls = rolls;
        return this;
    }

    public MutableLootPool bonusRolls(NumberProvider bonusRolls) {
        this.bonusRolls = bonusRolls;
        return this;
    }

    public LootConditionList getConditions() {
        return conditions;
    }

    public LootFunctionList getFunctions() {
        return functions;
    }

    public LootEntryList getEntries() {
        return entries;
    }

    public LootPool buildVanillaPool() {
        // TODO maybe just directly modify the vanilla fields and replace them with our lists.
        var functions = this.functions;
        var conditions = this.conditions;
        var entries = this.entries.createVanillaArray();

        if (vanillaPool != null) {
            var ext = (LootPoolExtension) vanillaPool;
            ext.lootjs$setConditions(conditions);
            ext.lootjs$setEntries(entries);
            ext.lootjs$setFunctions(functions);
            ext.lootjs$setRolls(rolls);
            ext.lootjs$setBonusRolls(bonusRolls);
            return vanillaPool;
        }

        return new LootPool(entries, conditions, functions, rolls, bonusRolls, Optional.empty()); // TODO
    }

    public void collectDebugInfo(DebugInfo info) {
        info.add("% Rolls -> " + getRollStr(rolls));
        info.add("% Bonus rolls -> " + getRollStr(bonusRolls));
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
    public void addEntry(LootEntry entry) {
        entries.add(entry);
    }

    @Override
    public void transformEntry(NullableFunction<SimpleLootEntry, Object> onTransform, boolean deepTransform) {
        getEntries().transformEntry(onTransform, deepTransform);
    }

    @Override
    public void removeEntry(Predicate<SimpleLootEntry> onRemove, boolean deepRemove) {
        getEntries().removeEntry(onRemove, deepRemove);
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
