package com.almostreliable.lootjs.kube;

import com.almostreliable.lootjs.core.LootContextParamSetsMapping;
import com.almostreliable.lootjs.core.LootContextType;
import com.almostreliable.lootjs.filters.ResourceLocationFilter;
import com.almostreliable.lootjs.loot.table.LootTableHolder;
import com.almostreliable.lootjs.loot.table.MutableLootTable;
import com.almostreliable.lootjs.mixin.LootDataManagerAccessor;
import com.almostreliable.lootjs.util.EntityTypeFilter;
import com.almostreliable.lootjs.util.Utils;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.event.EventResult;
import dev.latvian.mods.kubejs.util.ConsoleJS;
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
    private final Map<ResourceLocation, LootTableHolder> lootTableHolders = new HashMap<>();
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

    Set<ResourceLocation> getLootTableIds() {
        return getLootTableIds(location -> true);
    }

    Set<ResourceLocation> getLootTableIds(ResourceLocationFilter filter) {
        return getData()
                .keySet()
                .stream()
                .filter(id -> id.type() == LootDataType.TABLE)
                .map(LootDataId::location)
                .filter(filter)
                .collect(Collectors.toSet());
    }

    public void forEachTable(ResourceLocationFilter filter, Consumer<LootTableHolder> onForEach) {
        getLootTableIds(filter).forEach(location -> {
            LootTableHolder holder = getLootTable(location);
            if (holder != null) {
                onForEach.accept(holder);
            }
        });
    }

    public boolean hasLootTable(ResourceLocation location) {
        return getData().containsKey(new LootDataId<>(LootDataType.TABLE, location));
    }

    public void forEachTable(Consumer<LootTableHolder> onForEach) {
        forEachTable(location -> true, onForEach);
    }

    public void clearLootTables(ResourceLocationFilter filter) {
        for (LootDataId<?> id : getData().keySet()) {
            if (id.type() != LootDataType.TABLE) {
                continue;
            }

            LootTableHolder holder = getLootTable(id.location());
            if (holder != null) {
                holder.clear();
            }
        }
    }

    @Nullable
    public LootTableHolder getLootTable(ResourceLocation location) {
        var dataId = new LootDataId<>(LootDataType.TABLE, location);
        if (!getData().containsKey(dataId)) {
            return null;
        }

        return lootTableHolders.computeIfAbsent(location, rl -> {
            var entry = getData().get(dataId);
            if (entry instanceof LootTable table) {
                return new LootTableHolder(table, location);
            }

            throw new RuntimeException("[Internal LootJS Error] Loot table not found: " + rl);
        });
    }

    public void modifyLootTable(ResourceLocationFilter filter, Consumer<MutableLootTable> onModify) {
        getData().forEach((id, entry) -> {
            if (!filter.test(id.location())) {
                return;
            }

            LootTableHolder holder = getLootTable(id.location());
            if (holder != null) {
                holder.modify(onModify);
            }
        });
    }

    public void modifyBlockLoot(BlockStatePredicate filter, Consumer<MutableLootTable> onModify) {
        Set<ResourceLocation> ids = filter.getBlocks().stream().map(Block::getLootTable).collect(Collectors.toSet());
        for (ResourceLocation id : ids) {
            LootTableHolder holder = getLootTable(id);
            if (holder != null) {
                holder.modify(onModify);
            }
        }
    }

    public void modifyEntityLoot(EntityTypeFilter filter, Consumer<MutableLootTable> onModify) {
        Set<ResourceLocation> ids = filter
                .getEntityTypes()
                .stream()
                .map(EntityType::getDefaultLootTable).collect(Collectors.toSet());
        for (ResourceLocation id : ids) {
            LootTableHolder holder = getLootTable(id);
            if (holder != null) {
                holder.modify(onModify);
            }
        }
    }

    public void modifyLootType(LootContextType type, Consumer<MutableLootTable> onModify) {
        modifyLootType(new LootContextType[]{ type }, onModify);
    }

    public void modifyLootType(LootContextType[] types, Consumer<MutableLootTable> onModify) {
        Set<LootContextType> asSet = new HashSet<>(Arrays.asList(types));
        getData().forEach((id, entry) -> {
            if (!(entry instanceof LootTable table)) {
                return;
            }

            LootContextType type = LootContextParamSetsMapping.PSETS_TO_TYPE.getOrDefault(table.getParamSet(),
                    LootContextType.UNKNOWN);
            if (!asSet.contains(type)) {
                return;
            }

            LootTableHolder holder = getLootTable(id.location());
            if (holder != null) {
                holder.modify(onModify);
            }
        });
    }

    public void create(ResourceLocation location, Consumer<MutableLootTable> onCreate) {
        create(location, LootContextType.CHEST, onCreate);
    }

    public void create(ResourceLocation location, LootContextType type, Consumer<MutableLootTable> onCreate) {
        if (hasLootTable(location)) {
            throw new RuntimeException("[LootJS Error] Loot table already exists, cannot create new one: " + location);
        }

        LootContextParamSet paramSet = LootContextParamSetsMapping.TYPE_TO_PSETS.get(type);
        if (paramSet == null) {
            ConsoleJS.SERVER.error("[LootJS Error] Unknown loot context type: " + type);
            ConsoleJS.SERVER.error("Available types: " + LootContextParamSetsMapping.TYPE_TO_PSETS.keySet());
            return;
        }

        unwrapData();
        LootTable lootTable = new LootTable.Builder().setParamSet(paramSet).setRandomSequence(location).build();
        getData().put(new LootDataId<>(LootDataType.TABLE, location), Utils.cast(lootTable));
        LootTableHolder holder = new LootTableHolder(lootTable, location, true);
        LootTableHolder old = lootTableHolders.put(location, holder);
        if (old != null) {
            throw new IllegalStateException("Loot table already exists: " + location);
        }

        onCreate.accept(holder.mutate());
    }

    @Override
    protected void afterPosted(EventResult result) {
        super.afterPosted(result);

        lootTableHolders.forEach((location, holder) -> {
            try {
                holder.ifChanged(MutableLootTable::writeToVanillaTable);
                ConsoleJS.SERVER.info("Re-applied modified loot table to vanilla: " + location);
            } catch (Exception e) {
                ConsoleJS.SERVER.error("Error while re-applying modified loot table to vanilla: " + location, e);
            }
        });

        if (unwrappedData != null) {
            ((LootDataManagerAccessor) manager).setElements(unwrappedData);
        }
    }
}
