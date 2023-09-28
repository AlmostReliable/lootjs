package com.almostreliable.lootjs.loot.table;

import com.almostreliable.lootjs.LootJS;
import com.almostreliable.lootjs.LootJSPlatform;
import com.almostreliable.lootjs.loot.table.entry.LootAppendHelper;
import com.almostreliable.lootjs.loot.table.entry.LootContainer;
import com.almostreliable.lootjs.loot.table.entry.LootEntry;
import com.almostreliable.lootjs.loot.table.entry.LootTransformHelper;
import com.almostreliable.lootjs.util.DebugInfo;
import com.almostreliable.lootjs.util.NullableFunction;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import javax.annotation.Nullable;
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
        entries = new LootEntryList(accessor.lootjs$getEntries());
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
        var functions = this.functions.createVanillaArray();
        var conditions = this.conditions.createVanillaArray();
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

        return LootJSPlatform.INSTANCE.createLootPool(entries, conditions, functions, rolls, bonusRolls, null);
    }

    public void collectDebugInfo(DebugInfo info) {
        info.add("% Rolls -> " + getRollStr(rolls));
        info.add("% Bonus rolls -> " + getRollStr(bonusRolls));
        getEntries().collectDebugInfo(info);
        getConditions().collectDebugInfo(info);
        getFunctions().collectDebugInfo(info);
    }

    private String getRollStr(NumberProvider numberProvider) {
        JsonElement tree = LootJS.CONDITION_GSON.toJsonTree(numberProvider);
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
    public void addEntry(LootContainer entry) {
        entries.add(entry);
    }

    @Override
    public void transformLoot(NullableFunction<LootEntry, Object> onTransform, boolean deepTransform) {
        getEntries().transformLoot(onTransform, deepTransform);
    }

    @Override
    public void removeLoot(Predicate<LootEntry> onRemove, boolean deepRemove) {
        getEntries().removeLoot(onRemove, deepRemove);
    }

    public MutableLootPool when(Consumer<LootConditionList> onConditions) {
        onConditions.accept(getConditions());
        return this;
    }

    public MutableLootPool modifiers(Consumer<LootFunctionList> onModifiers) {
        onModifiers.accept(getFunctions());
        return this;
    }
}
