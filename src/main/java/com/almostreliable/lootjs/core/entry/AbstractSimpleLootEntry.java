package com.almostreliable.lootjs.core.entry;

import com.almostreliable.lootjs.loot.LootConditionList;
import com.almostreliable.lootjs.loot.LootFunctionList;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;

import javax.annotation.Nullable;

public abstract class AbstractSimpleLootEntry<E extends LootPoolSingletonContainer> implements SimpleLootEntry {

    protected final E vanillaEntry;
    @Nullable
    protected LootFunctionList functions;
    @Nullable
    protected LootConditionList conditions;

    public AbstractSimpleLootEntry(E vanillaEntry) {
        this.vanillaEntry = vanillaEntry;
    }

    @Override
    public LootPoolEntryType getVanillaType() {
        return vanillaEntry.getType();
    }

    @Override
    public E getVanillaEntry() {
        free();
        return vanillaEntry;
    }

    protected void free() {
        if (conditions != null) {
            vanillaEntry.conditions = conditions; // TODO check if this is needed
            vanillaEntry.compositeCondition = LootItemConditions.andConditions(vanillaEntry.conditions);
            conditions = null;
        }

        if (functions != null) {
            vanillaEntry.functions = functions;
            vanillaEntry.compositeFunction = LootItemFunctions.compose(vanillaEntry.functions);
            functions = null;
        }
    }

    public LootFunctionList getFunctions() {
        if (functions == null) {
            functions = new LootFunctionList(vanillaEntry.functions);
        }

        return functions;
    }

    @Override
    public LootConditionList getConditions() {
        if (conditions == null) {
            conditions = new LootConditionList(vanillaEntry.conditions);
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
}
