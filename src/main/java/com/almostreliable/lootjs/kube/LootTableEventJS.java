package com.almostreliable.lootjs.kube;

import com.almostreliable.lootjs.core.LootType;
import com.almostreliable.lootjs.core.filters.ResourceLocationFilter;
import com.almostreliable.lootjs.loot.table.MutableLootTable;
import com.almostreliable.lootjs.mixin.LootDataManagerAccessor;
import com.almostreliable.lootjs.util.Utils;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.event.EventResult;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class LootTableEventJS extends EventJS {

    private final Map<LootDataId<?>, ?> rawData;
    private final Map<ResourceLocation, MutableLootTable> lootTableHolders = new HashMap<>();
    private final LootDataManager manager;
    @Nullable private Map<LootDataId<?>, ?> unwrappedData;

    public LootTableEventJS(LootDataManager manager) {
        this.manager = manager;
        LootDataManagerAccessor accessor = (LootDataManagerAccessor) manager;
        rawData = accessor.getElements();
    }

    private Map<LootDataId<?>, ?> getData() {
        if (unwrappedData != null) {
            return unwrappedData;
        }

        return rawData;
    }

    private void unwrapData() {
        if (unwrappedData != null) {
            return;
        }

        unwrappedData = new HashMap<>();
        rawData.forEach((id, entry) -> unwrappedData.put(id, Utils.cast(entry)));
    }

    public Set<ResourceLocation> getLootTableIds() {
        return getLootTableIds(location -> true);
    }

    public Set<ResourceLocation> getLootTableIds(ResourceLocationFilter filter) {
        return getData()
                .keySet()
                .stream()
                .filter(id -> id.type() == LootDataType.TABLE)
                .map(LootDataId::location)
                .filter(filter)
                .collect(Collectors.toSet());
    }

    public void forEachTable(ResourceLocationFilter filter, Consumer<MutableLootTable> onForEach) {
        getLootTableIds(filter).forEach(location -> {
            var table = getLootTable(location);
            if (table != null) {
                onForEach.accept(table);
            }
        });
    }

    public void forEachTable(Consumer<MutableLootTable> onForEach) {
        forEachTable(location -> true, onForEach);
    }

    public boolean hasLootTable(ResourceLocation location) {
        return getData().containsKey(new LootDataId<>(LootDataType.TABLE, location));
    }

    public void clearLootTables(ResourceLocationFilter filter) {
        for (LootDataId<?> id : getData().keySet()) {
            if (id.type() != LootDataType.TABLE) {
                continue;
            }

            var table = getLootTable(id.location());
            if (table != null) {
                table.clear();
            }
        }
    }

    @Nullable
    public MutableLootTable getLootTable(ResourceLocation location) {
        var dataId = new LootDataId<>(LootDataType.TABLE, location);
        if (!getData().containsKey(dataId)) {
            return null;
        }

        return lootTableHolders.computeIfAbsent(location, rl -> {
            var entry = getData().get(dataId);
            if (entry instanceof LootTable table) {
                return new MutableLootTable(table, location);
            }

            throw new RuntimeException("[Internal LootJS Error] Loot table not found: " + rl);
        });
    }

    @Nullable
    public MutableLootTable getBlockLoot(Block block) {
        return getLootTable(block.getLootTable());
    }

    @Nullable
    public MutableLootTable getEntityLoot(EntityType<?> entityType) {
        return getLootTable(entityType.getDefaultLootTable());
    }

    public void modifyLootTables(ResourceLocationFilter filter, Consumer<MutableLootTable> onModify) {
        getData().forEach((id, entry) -> {
            if (!filter.test(id.location())) {
                return;
            }

            var table = getLootTable(id.location());
            if (table != null) {
                onModify.accept(table);
            }
        });
    }

    public void modifyBlockLoot(BlockStatePredicate filter, Consumer<MutableLootTable> onModify) {
        for (Block block : filter.getBlocks()) {
            MutableLootTable table = getBlockLoot(block);
            if (table != null) {
                onModify.accept(table);
            }
        }
    }

    public void modifyEntityLoot(EntityTypePredicate filter, Consumer<MutableLootTable> onModify) {
        for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
            if (filter.matches(entityType)) {
                var table = getEntityLoot(entityType);
                if (table != null) {
                    onModify.accept(table);
                }
            }
        }
    }

    public void modifyLootType(LootType type, Consumer<MutableLootTable> onModify) {
        modifyLootType(new LootType[]{ type }, onModify);
    }

    public void modifyLootType(LootType[] types, Consumer<MutableLootTable> onModify) {
        Set<LootType> asSet = new HashSet<>(Arrays.asList(types));
        getData().forEach((id, entry) -> {
            if (!(entry instanceof LootTable vanillaTable)) {
                return;
            }

            LootType type = LootType.getLootType(vanillaTable.getParamSet());
            if (!asSet.contains(type)) {
                return;
            }

            var table = getLootTable(id.location());
            if (table != null) {
                onModify.accept(table);
            }
        });
    }

    public void create(ResourceLocation location, Consumer<MutableLootTable> onCreate) {
        create(location, LootType.CHEST, onCreate);
    }

    public void create(ResourceLocation location, LootType type, Consumer<MutableLootTable> onCreate) {
        if (hasLootTable(location)) {
            throw new RuntimeException("[LootJS Error] Loot table already exists, cannot create new one: " + location);
        }

        LootContextParamSet paramSet = type.getParamSet();
        unwrapData();
        LootTable lootTable = new LootTable.Builder().setParamSet(paramSet).setRandomSequence(location).build();
        getData().put(new LootDataId<>(LootDataType.TABLE, location), Utils.cast(lootTable));
        var table = new MutableLootTable(lootTable, location);
        table.markDirty();
        if (lootTableHolders.put(location, table) != null) {
            throw new IllegalStateException("Loot table already exists: " + location);
        }

        onCreate.accept(table);
    }

    @Override
    protected void afterPosted(EventResult result) {
        super.afterPosted(result);

        lootTableHolders.forEach((location, table) -> {
            try {
                if (table.isDirty()) {
                    table.writeToVanillaTable();
                    ConsoleJS.SERVER.info("Re-applied modified loot table to vanilla: " + location);
                }
            } catch (Exception e) {
                ConsoleJS.SERVER.error("Error while re-applying modified loot table to vanilla: " + location, e);
            }
        });

        if (unwrappedData != null) {
            ((LootDataManagerAccessor) manager).setElements(unwrappedData);
        }
    }
}
