package com.almostreliable.lootjs.loot.table;

import com.almostreliable.lootjs.core.LootType;
import com.almostreliable.lootjs.core.entry.LootEntry;
import com.almostreliable.lootjs.core.entry.SimpleLootEntry;
import com.almostreliable.lootjs.loot.LootFunctionList;
import com.almostreliable.lootjs.loot.extension.LootTableExtension;
import com.almostreliable.lootjs.util.DebugInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@SuppressWarnings("UnusedReturnValue")
public class MutableLootTable implements LootApplier {

    private final LootTable origin;
    private final ResourceLocation location;
    @Nullable
    private List<MutableLootPool> pools;
    @Nullable
    private LootFunctionList functions;
    private boolean potentialModified;

    public MutableLootTable(LootTable lootTable) {
        this(lootTable, lootTable.getLootTableId());
    }

    public MutableLootTable(LootTable lootTable, ResourceLocation location) {
        this.location = location;
        this.origin = lootTable;
    }

    public MutableLootTable(LootContextParamSet paramSet, ResourceLocation location) {
        this(new LootTable.Builder().setParamSet(paramSet).setRandomSequence(location).build(), location);
    }

    public ResourceLocation getRandomSequence() {
        ResourceLocation rs = LootTableExtension.cast(origin).lootjs$getRandomSequence();
        return rs == null ? getLocation() : rs;
    }

    public void setRandomSequence(@Nullable ResourceLocation randomSequence) {
        LootTableExtension.cast(origin).lootjs$setRandomSequence(randomSequence);
    }

    public LootType getLootType() {
        return LootType.getLootType(origin.getParamSet());
    }

    public ResourceLocation getLocation() {
        return location;
    }

    public List<MutableLootPool> getPools() {
        if (pools == null) {
            markDirty();
            pools = new ArrayList<>();
            for (LootPool pool : ((LootTableExtension) origin).lootjs$getPools()) {
                pools.add(new MutableLootPool(pool));
            }
        }
        return pools;
    }

    public MutableLootTable firstPool(Consumer<MutableLootPool> onModifyPool) {
        onModifyPool.accept(firstPool());
        return this;
    }

    public MutableLootPool firstPool() {
        var pools = getPools();
        if (pools.isEmpty()) {
            return createPool();
        }

        return pools.get(0);
    }

    public MutableLootTable createPool(Consumer<MutableLootPool> onCreatePool) {
        onCreatePool.accept(createPool());
        return this;
    }

    public MutableLootPool createPool() {
        var pool = new MutableLootPool();
        getPools().add(pool);
        return pool;
    }

    public LootFunctionList getFunctions() {
        if (functions == null) {
            markDirty();
            functions = LootTableExtension.cast(origin).lootjs$createFunctionList();
        }

        return functions;
    }

    public MutableLootTable onDrop(PostLootAction postLootAction) {
        if (origin instanceof PostLootActionOwner owner) {
            owner.lootjs$setPostLootAction(postLootAction);
        }

        return this;
    }

    public void writeToVanillaTable() {
        if (pools != null) {
            LootTableExtension.cast(origin).lootjs$setPools(getPools().stream()
                    .map(MutableLootPool::getVanillaPool)
                    .toList());
        }
    }

    public void print() {
        DebugInfo info = new DebugInfo();
        info.add("Loot table: " + location);
        info.push();

        info.add("% Pools [");
        info.push();
        var pools = this.pools;
        if (pools == null) {
            List<LootPool> lootPools = LootTableExtension.cast(origin).lootjs$getPools();
            pools = lootPools.stream().map(MutableLootPool::new).toList();
        }

        for (MutableLootPool pool : pools) {
            info.add("{");
            info.push();
            pool.collectDebugInfo(info);
            info.pop();
            info.add("}");
        }

        info.pop();
        info.add("]");
        var f = functions == null ? LootTableExtension.cast(origin).lootjs$createFunctionList()
                                  : functions;
        f.collectDebugInfo(info);
        info.pop();

        info.release();
    }

    public MutableLootTable clear() {
        getFunctions().clear();
        getPools().clear();
        return this;
    }

    @Override
    public MutableLootTable addEntry(LootEntry entry) {
        createPool(pool -> pool.addEntry(entry));
        return this;
    }

    @Override
    public MutableLootTable transformEntry(UnaryOperator<SimpleLootEntry> onTransform, boolean deepTransform) {
        for (MutableLootPool pool : getPools()) {
            pool.transformEntry(onTransform, deepTransform);
        }

        return this;
    }

    @Override
    public MutableLootTable removeEntry(Predicate<SimpleLootEntry> onRemove, boolean deepRemove) {
        for (MutableLootPool pool : getPools()) {
            pool.removeEntry(onRemove, deepRemove);
        }

        return this;
    }

    public MutableLootTable apply(Consumer<LootFunctionList> onModifiers) {
        onModifiers.accept(getFunctions());
        return this;
    }

    public boolean isDirty() {
        return potentialModified;
    }

    public void markDirty() {
        potentialModified = true;
    }
}
