package com.almostreliable.lootjs.loot.table;

import com.almostreliable.lootjs.core.LootType;
import com.almostreliable.lootjs.core.entry.LootEntry;
import com.almostreliable.lootjs.core.entry.SimpleLootEntry;
import com.almostreliable.lootjs.loot.LootFunctionList;
import com.almostreliable.lootjs.loot.extension.LootPoolExtension;
import com.almostreliable.lootjs.loot.extension.LootTableExtension;
import com.almostreliable.lootjs.util.DebugInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@SuppressWarnings("UnusedReturnValue")
public class MutableLootTable implements LootApplier {

    private final LootTable origin;
    private final ResourceLocation location;
    @Nullable private LootFunctionList functions;

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

    private List<LootPool> getVanillaPools() {
        return LootTableExtension.cast(origin).lootjs$getPools();
    }

    public MutableLootTable firstPool(Consumer<MutableLootPool> onModifyPool) {
        onModifyPool.accept(firstPool());
        return this;
    }

    public MutableLootPool firstPool() {
        var pools = LootTableExtension.cast(origin).lootjs$getPools();
        if (pools.isEmpty()) {
            return createPool();
        }

        return new MutableLootPool(pools.get(0));
    }

    public MutableLootTable createPool(Consumer<MutableLootPool> onCreatePool) {
        onCreatePool.accept(createPool());
        return this;
    }

    public MutableLootPool createPool() {
        var pool = new LootPool(new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                ConstantValue.exactly(1),
                ConstantValue.exactly(0),
                Optional.empty());
        getVanillaPools().add(pool);
        return new MutableLootPool(pool);
    }

    public LootFunctionList getFunctions() {
        if (functions == null) {
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
        // TODO remove
    }

    public void print() {
        DebugInfo info = new DebugInfo();
        info.add("Loot table: " + location);
        info.push();

        info.add("% Pools [");
        info.push();

        for (var pool : getVanillaPools()) {
            info.add("{");
            info.push();
            LootPoolExtension.cast(pool).lootjs$collectDebugInfo(info);
            info.pop();
            info.add("}");
        }

        info.pop();
        info.add("]");
        var f = functions == null ? LootTableExtension.cast(origin).lootjs$createFunctionList() : functions;
        f.collectDebugInfo(info);
        info.pop();

        info.release();
    }

    public MutableLootTable clear() {
        getFunctions().clear();
        getVanillaPools().clear();
        return this;
    }

    @Override
    public MutableLootTable addEntry(LootEntry entry) {
        createPool(pool -> pool.addEntry(entry));
        return this;
    }

    @Override
    public MutableLootTable modifyEntry(UnaryOperator<SimpleLootEntry> onTransform, boolean deepTransform) {
        for (var pool : getVanillaPools()) {
            new MutableLootPool(pool).modifyEntry(onTransform, deepTransform);
        }

        return this;
    }

    @Override
    public MutableLootTable removeEntry(Predicate<SimpleLootEntry> onRemove, boolean deepRemove) {
        for (var pool : getVanillaPools()) {
            new MutableLootPool(pool).removeEntry(onRemove, deepRemove);
        }

        return this;
    }

    public MutableLootTable apply(Consumer<LootFunctionList> onModifiers) {
        onModifiers.accept(getFunctions());
        return this;
    }
}
