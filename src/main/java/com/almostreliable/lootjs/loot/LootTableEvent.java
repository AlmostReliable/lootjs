package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.core.LootType;
import com.almostreliable.lootjs.core.filters.IdFilter;
import com.almostreliable.lootjs.core.filters.LootTableFilter;
import com.almostreliable.lootjs.loot.table.LootTableList;
import com.almostreliable.lootjs.loot.table.MutableLootTable;
import com.mojang.serialization.Lifecycle;
import dev.latvian.mods.kubejs.script.ConsoleJS;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class LootTableEvent {
    private static final RegistrationInfo REG_INFO = new RegistrationInfo(Optional.empty(), Lifecycle.stable());
    private final WritableRegistry<LootTable> registry;

    public LootTableEvent(WritableRegistry<LootTable> registry) {
        this.registry = registry;
    }

    protected WritableRegistry<LootTable> registry() {
        return registry;
    }

    public Set<Identifier> getLootTableIds() {
        return Collections.unmodifiableSet(registry().keySet());
    }

    public Set<Identifier> getLootTableIds(IdFilter filter) {
        return registry().keySet().stream().filter(filter).collect(Collectors.toSet());
    }

    public void forEachTable(IdFilter filter, Consumer<MutableLootTable> onForEach) {
        getLootTableIds(filter).forEach(location -> {
            var table = getLootTable(location);
            onForEach.accept(table);
        });
    }

    public void forEachTable(Consumer<MutableLootTable> onForEach) {
        forEachTable(location -> true, onForEach);
    }

    public boolean hasLootTable(Identifier location) {
        return registry().containsKey(location);
    }

    public void clearLootTables(IdFilter filter) {
        for (LootTable lootTable : registry()) {
            if (filter.test(lootTable.getLootTableId())) {
                new MutableLootTable(lootTable).clear();
            }
        }
    }

    public MutableLootTable getLootTable(Identifier location) {
        LootTable lootTable = registry().getValue(location);
        if (lootTable == null) {
            throw new IllegalArgumentException("Unknown loot table: " + location);
        }

        return new MutableLootTable(lootTable);
    }

    public MutableLootTable getBlockTable(Block block) {
        var tableId = block.getLootTable().orElseThrow().identifier();
        return getLootTable(tableId);
    }

    public MutableLootTable getEntityTable(EntityType<?> entityType) {
        var tableId = entityType.getDefaultLootTable().orElseThrow().identifier();
        return getLootTable(tableId);
    }

    public LootTableList modifyLootTables(LootTableFilter... filters) {
        var tables = registry()
                .stream()
                .filter(lootTable -> {
                    for (var f : filters) {
                        if (f.test(lootTable)) {
                            return true;
                        }
                    }

                    return false;
                })
                .map(MutableLootTable::new)
                .toList();
        return new LootTableList(tables);
    }

    public LootTableList modifyBlockTables(IdFilter filter) {
        var tables = BuiltInRegistries.BLOCK
                .listElements()
                .filter(ref -> filter.test(ref.key().identifier()))
                .map(ref -> this.getBlockTable(ref.value()))
                .toList();
        return new LootTableList(tables);
    }

    public LootTableList modifyEntityTables(IdFilter filter) {
        var tables = BuiltInRegistries.ENTITY_TYPE
                .listElements()
                .filter(ref -> filter.test(ref.key().identifier()))
                .map(ref -> this.getEntityTable(ref.value()))
                .toList();
        return new LootTableList(tables);
    }

    @Deprecated(forRemoval = true)
    public LootTableList modifyLootTypeTables(LootType... types) {
        ConsoleJS.SERVER.error(
                "LootJS: `modifyLootTypeTables` is deprecated. Use `modifyLootTables` with LootType instead.");
        Set<LootType> asSet = new HashSet<>(Arrays.asList(types));

        var tables = registry().stream().filter(lootTable -> {
            LootType type = LootType.getLootType(lootTable.getParamSet());
            return asSet.contains(type);
        }).map(MutableLootTable::new).toList();
        return new LootTableList(tables);
    }

    public MutableLootTable create(Identifier location) {
        return create(location, LootType.CHEST);
    }

    public MutableLootTable create(Identifier location, LootType type) {
        if (hasLootTable(location)) {
            throw new RuntimeException("[LootJS Error] Loot table already exists, cannot create new one: " + location);
        }

        var paramSet = type.getParamSet();
        var lootTable = new LootTable.Builder().setParamSet(paramSet).setRandomSequence(location).build();
        //noinspection ConstantValue
        if (lootTable.getLootTableId() == null) {
            lootTable.setLootTableId(location);
        }

        var key = ResourceKey.create(Registries.LOOT_TABLE, location);
        registry().register(key, lootTable, REG_INFO);
        return new MutableLootTable(lootTable);
    }
}
