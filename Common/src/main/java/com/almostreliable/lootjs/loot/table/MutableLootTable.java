package com.almostreliable.lootjs.loot.table;

import com.almostreliable.lootjs.loot.table.entry.LootEntry;
import com.almostreliable.lootjs.loot.table.entry.LootTransformHelper;
import com.almostreliable.lootjs.util.DebugInfo;
import com.almostreliable.lootjs.util.NullableFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class MutableLootTable implements LootTransformHelper {

    private final LootTable origin;
    private final List<MutableLootPool> pools;
    private final LootFunctionList functions;
    private final ResourceLocation location;
    @Nullable
    private ResourceLocation randomSequence;

    public MutableLootTable(LootTable lootTable, ResourceLocation location) {
        this.location = location;
        this.origin = lootTable;
        var lt = (LootTableExtension) lootTable;

        pools = new ArrayList<>();
        for (LootPool pool : lt.lootjs$getPools()) {
            pools.add(new MutableLootPool(pool));
        }

        functions = new LootFunctionList(lt.lootjs$getFunctions());
        randomSequence = lt.lootjs$getRandomSequence();
    }

    public MutableLootTable(LootContextParamSet paramSet, ResourceLocation location) {
        this(new LootTable.Builder().setParamSet(paramSet).setRandomSequence(location).build(), location);
    }

    public ResourceLocation getRandomSequence() {
        return randomSequence == null ? getLocation() : randomSequence;
    }

    public void setRandomSequence(@Nullable ResourceLocation randomSequence) {
        this.randomSequence = randomSequence;
    }

    public ResourceLocation getLocation() {
        return location;
    }

    public List<MutableLootPool> getPools() {
        return pools;
    }

    public MutableLootTable firstPool(Consumer<MutableLootPool> onModifyPool) {
        if (pools.isEmpty()) {
            return addPool(onModifyPool);
        }

        onModifyPool.accept(pools.get(0));
        return this;
    }

    public MutableLootTable addPool(Consumer<MutableLootPool> onAddPool) {
        MutableLootPool pool = new MutableLootPool();
        onAddPool.accept(pool);
        pools.add(pool);
        return this;
    }

    public LootFunctionList getFunctions() {
        return functions;
    }

    public void writeToVanillaTable() {
        var pools = this.pools.stream()
                .map(MutableLootPool::buildVanillaPool)
                .toList();
        var functions = this.functions.createVanillaArray();

        var lt = (LootTableExtension) origin;
        lt.lootjs$setPools(pools);
        lt.lootjs$setFunctions(functions);
        lt.lootjs$setRandomSequence(getRandomSequence());
    }

    public void print() {
        DebugInfo info = new DebugInfo();
        info.add("Loot table: " + location);
        info.push();

        info.add("% Pools [");
        info.push();
        for (MutableLootPool pool : pools) {
            info.add("{");
            info.push();
            pool.collectDebugInfo(info);
            info.pop();
            info.add("}");
        }

        info.pop();
        info.add("]");
        getFunctions().collectDebugInfo(info);
        info.pop();

        info.release();
    }

    public void clear() {
        getFunctions().clear();
        getPools().clear();
    }

    @Override
    public void transformLoot(NullableFunction<LootEntry, Object> onTransform, boolean deepTransform) {
        for (MutableLootPool pool : getPools()) {
            pool.transformLoot(onTransform, deepTransform);
        }
    }

    @Override
    public void removeLoot(Predicate<LootEntry> onRemove, boolean deepRemove) {
        for (MutableLootPool pool : getPools()) {
            pool.removeLoot(onRemove, deepRemove);
        }
    }

    public MutableLootTable modifiers(Consumer<LootFunctionList> onModifiers) {
        onModifiers.accept(getFunctions());
        return this;
    }
}
