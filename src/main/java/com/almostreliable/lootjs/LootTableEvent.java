package com.almostreliable.lootjs;

import com.almostreliable.lootjs.core.LootType;
import com.almostreliable.lootjs.core.filters.ResourceLocationFilter;
import com.almostreliable.lootjs.loot.table.LootTableList;
import com.almostreliable.lootjs.loot.table.MutableLootTable;
import com.almostreliable.lootjs.util.BlockFilter;
import com.mojang.serialization.Lifecycle;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public interface LootTableEvent {
    RegistrationInfo REG_INFO = new RegistrationInfo(Optional.empty(), Lifecycle.stable());

    WritableRegistry<LootTable> registry();


    default Set<ResourceLocation> getLootTableIds() {
        return Collections.unmodifiableSet(registry().keySet());
    }

    default Set<ResourceLocation> getLootTableIds(ResourceLocationFilter filter) {
        return registry().keySet().stream().filter(filter).collect(Collectors.toSet());
    }

    default void forEachTable(ResourceLocationFilter filter, Consumer<MutableLootTable> onForEach) {
        getLootTableIds(filter).forEach(location -> {
            var table = getLootTable(location);
            onForEach.accept(table);
        });
    }

    default void forEachTable(Consumer<MutableLootTable> onForEach) {
        forEachTable(location -> true, onForEach);
    }

    default boolean hasLootTable(ResourceLocation location) {
        return registry().containsKey(location);
    }

    default void clearLootTables(ResourceLocationFilter filter) {
        for (LootTable lootTable : registry()) {
            if (filter.test(lootTable.getLootTableId())) {
                new MutableLootTable(lootTable).clear();
            }
        }
    }

    default MutableLootTable getLootTable(ResourceKey<LootTable> key) {
        LootTable lootTable = registry().get(key);
        if (lootTable == null) {
            throw new IllegalArgumentException("Unknown loot table: " + key.location());
        }

        return new MutableLootTable(lootTable);
    }

    default MutableLootTable getLootTable(ResourceLocation location) {
        LootTable lootTable = registry().get(location);
        if (lootTable == null) {
            throw new IllegalArgumentException("Unknown loot table: " + location);
        }

        return new MutableLootTable(lootTable);
    }

    @Nullable
    default MutableLootTable getBlockTable(Block block) {
        return getLootTable(block.getLootTable());
    }

    @Nullable
    default MutableLootTable getEntityTable(EntityType<?> entityType) {
        return getLootTable(entityType.getDefaultLootTable());
    }

    default LootTableList modifyLootTables(ResourceLocationFilter filter) {
        var tables = registry()
                .stream()
                .filter(lootTable -> filter.test(lootTable.getLootTableId()))
                .map(MutableLootTable::new)
                .toList();
        return new LootTableList(tables);
    }

    default LootTableList modifyBlockTables(BlockFilter filter) {
        var tables = filter.stream().map(this::getBlockTable).toList();
        return new LootTableList(tables);
    }

    default LootTableList modifyEntityTables(EntityTypePredicate filter) {
        var tables = BuiltInRegistries.ENTITY_TYPE.stream().filter(filter::matches).map(this::getEntityTable).toList();
        return new LootTableList(tables);
    }

    default LootTableList modifyLootTypeTables(LootType... types) {
        Set<LootType> asSet = new HashSet<>(Arrays.asList(types));

        var tables = registry().stream().filter(lootTable -> {
            LootType type = LootType.getLootType(lootTable.getParamSet());
            return asSet.contains(type);
        }).map(MutableLootTable::new).toList();
        return new LootTableList(tables);
    }

    default MutableLootTable create(ResourceLocation location) {
        return create(location, LootType.CHEST);
    }

    default MutableLootTable create(ResourceLocation location, LootType type) {
        if (hasLootTable(location)) {
            throw new RuntimeException("[LootJS Error] Loot table already exists, cannot create new one: " + location);
        }

        LootContextParamSet paramSet = type.getParamSet();
        LootTable lootTable = new LootTable.Builder().setParamSet(paramSet).setRandomSequence(location).build();
        //noinspection ConstantValue
        if (lootTable.getLootTableId() == null) {
            lootTable.setLootTableId(location);
        }

        var key = ResourceKey.create(Registries.LOOT_TABLE, location);
        registry().register(key, lootTable, REG_INFO);
        return new MutableLootTable(lootTable);
    }
}
