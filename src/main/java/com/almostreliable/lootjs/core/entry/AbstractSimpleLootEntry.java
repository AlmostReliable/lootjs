package com.almostreliable.lootjs.core.entry;

import com.almostreliable.lootjs.loot.LootConditionList;
import com.almostreliable.lootjs.loot.LootFunctionList;
import com.almostreliable.lootjs.util.DebugInfo;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;

import javax.annotation.Nullable;

public abstract class AbstractSimpleLootEntry<E extends LootPoolSingletonContainer> implements SimpleLootEntry {

    protected final E vanillaEntry;
    @Nullable
    private LootConditionList conditions;
    @Nullable
    private LootFunctionList functions;

    public AbstractSimpleLootEntry(E vanillaEntry) {
        this.vanillaEntry = vanillaEntry;
    }

    public AbstractSimpleLootEntry(E vanillaEntry, LootConditionList conditions, LootFunctionList functions) {
        this(vanillaEntry);
        this.conditions = conditions;
        this.functions = functions;
    }

    @Override
    public LootPoolEntryType getVanillaType() {
        return vanillaEntry.getType();
    }

    @Override
    public E getVanillaEntry() {
        return vanillaEntry;
    }

    public LootFunctionList getFunctions() {
        if (functions == null) {
            functions = new LootFunctionList(vanillaEntry.functions);
            vanillaEntry.functions = functions.getElements();
            vanillaEntry.compositeFunction = functions;
        }

        return functions;
    }

    @Override
    public LootConditionList getConditions() {
        if (conditions == null) {
            conditions = new LootConditionList(vanillaEntry.conditions);
            vanillaEntry.conditions = conditions.getElements();
            vanillaEntry.compositeCondition = conditions;
        }

        return conditions;
    }

    public void setWeight(int weight) {
        vanillaEntry.weight = Math.max(1, weight);
    }

    public int getWeight() {
        return vanillaEntry.weight;
    }

    public void setQuality(int quality) {
        vanillaEntry.quality = Math.max(0, quality);
    }

    public int getQuality() {
        return vanillaEntry.quality;
    }

    @Override
    public void collectDebugInfo(DebugInfo info) {
        info.push();
        when(conditions -> conditions.collectDebugInfo(info));
        getFunctions().collectDebugInfo(info);
        info.pop();
    }
}
