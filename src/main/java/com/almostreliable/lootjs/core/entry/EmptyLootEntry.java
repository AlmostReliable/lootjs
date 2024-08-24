package com.almostreliable.lootjs.core.entry;

import com.almostreliable.lootjs.util.DebugInfo;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class EmptyLootEntry extends AbstractSimpleLootEntry<EmptyLootItem> implements SingleLootEntry {
    public EmptyLootEntry(EmptyLootItem vanillaEntry) {
        super(vanillaEntry);
    }

    public EmptyLootEntry() {
        super(new EmptyLootItem(
                LootPoolSingletonContainer.DEFAULT_WEIGHT,
                LootPoolSingletonContainer.DEFAULT_QUALITY,
                EMPTY_CONDITIONS,
                EMPTY_FUNCTIONS));
    }

    @Override
    public EmptyLootEntry addCondition(LootItemCondition condition) {
        getConditions().add(condition);
        return this;
    }

    @Override
    public void collectDebugInfo(DebugInfo info) {
        info.add("% Empty");
        super.collectDebugInfo(info);
    }
}
