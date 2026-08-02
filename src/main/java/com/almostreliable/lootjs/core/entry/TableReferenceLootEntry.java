package com.almostreliable.lootjs.core.entry;

import com.almostreliable.lootjs.util.DebugInfo;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class TableReferenceLootEntry extends AbstractSimpleLootEntry<NestedLootTable> {
    public TableReferenceLootEntry(NestedLootTable vanillaEntry) {
        super(vanillaEntry);
    }

    public TableReferenceLootEntry(Identifier location) {
        super(new NestedLootTable(Either.left(ResourceKey.create(Registries.LOOT_TABLE, location)),
                LootPoolSingletonContainer.DEFAULT_WEIGHT,
                LootPoolSingletonContainer.DEFAULT_QUALITY,
                EMPTY_CONDITIONS,
                EMPTY_FUNCTIONS));
    }

    public Identifier getLocation() {
        return vanillaEntry.contents.map(ResourceKey::identifier, LootTable::getLootTableId);
    }

    public void setLocation(Identifier reference) {
        vanillaEntry.contents = Either.left(ResourceKey.create(Registries.LOOT_TABLE, reference));
    }

    @Override
    public TableReferenceLootEntry addCondition(LootItemCondition condition) {
        getConditions().add(condition);
        return this;
    }

    @Override
    public void collectDebugInfo(DebugInfo info) {
        info.add("% Table: " + getLocation());
        super.collectDebugInfo(info);
    }
}
