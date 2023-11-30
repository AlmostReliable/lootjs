package com.almostreliable.lootjs.loot.table;

import com.almostreliable.lootjs.loot.LootFunctionList;
import com.almostreliable.lootjs.loot.extension.LootTableExtension;
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

@SuppressWarnings("UnusedReturnValue")
public class MutableLootTable implements LootTransformHelper {

    private final LootTable origin;
    private final ResourceLocation location;
    @Nullable
    private List<MutableLootPool> pools;
    @Nullable
    private LootFunctionList functions;
    @Nullable
    private ResourceLocation randomSequence;
    private boolean potentialModified;
    @Nullable
    private PostLootAction postLootAction;

    public MutableLootTable(LootTable lootTable, ResourceLocation location) {
        this.location = location;
        this.origin = lootTable;
        randomSequence = ((LootTableExtension) lootTable).lootjs$getRandomSequence();
    }

    public MutableLootTable(LootContextParamSet paramSet, ResourceLocation location) {
        this(new LootTable.Builder().setParamSet(paramSet).setRandomSequence(location).build(), location);
    }

    private void initialize() {
        if (pools == null) {
            pools = new ArrayList<>();
            for (LootPool pool : ((LootTableExtension) origin).lootjs$getPools()) {
                pools.add(new MutableLootPool(pool));
            }
        }

        if (functions == null) {
            functions = new LootFunctionList(((LootTableExtension) origin).lootjs$getFunctions());
        }
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
        initialize();
        markDirty();
        assert pools != null;

        return pools;
    }

    public MutableLootTable firstPool(Consumer<MutableLootPool> onModifyPool) {
        initialize();
        markDirty();
        assert pools != null;

        if (pools.isEmpty()) {
            return addPool(onModifyPool);
        }

        onModifyPool.accept(pools.get(0));
        return this;
    }

    public MutableLootTable addPool(Consumer<MutableLootPool> onAddPool) {
        initialize();
        markDirty();
        assert pools != null;

        MutableLootPool pool = new MutableLootPool();
        onAddPool.accept(pool);
        pools.add(pool);
        return this;
    }

    public LootFunctionList getFunctions() {
        initialize();
        markDirty();
        assert functions != null;

        return functions;
    }

    public MutableLootTable onDrop(PostLootAction postLootAction) {
        this.postLootAction = postLootAction;
        return this;
    }

    public void writeToVanillaTable() {
        if (!potentialModified) {
            return;
        }

        var pools = getPools().stream()
                .map(MutableLootPool::buildVanillaPool)
                .toList();
        var functions = getFunctions().createVanillaArray();

        var lt = (LootTableExtension) origin;
        lt.lootjs$setPools(pools);
        lt.lootjs$setFunctions(functions);
        lt.lootjs$setRandomSequence(getRandomSequence());

        if(postLootAction != null && origin instanceof PostLootActionOwner owner) {
            owner.lootjs$setPostLootAction(postLootAction);
        }
    }

    public void print() {
        initialize();
        assert pools != null;

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

    public MutableLootTable clear() {
        getFunctions().clear();
        getPools().clear();
        return this;
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

    public boolean isDirty() {
        return potentialModified;
    }

    public void markDirty() {
        potentialModified = true;
    }
}
