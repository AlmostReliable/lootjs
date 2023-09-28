package com.almostreliable.lootjs.loot.table;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class LootTableHolder {

    private final LootTable lootTable;
    private final ResourceLocation location;
    @Nullable private MutableLootTable mutableLootTable;
    private final boolean isNew;

    public LootTableHolder(LootTable lootTable, ResourceLocation location) {
        this(lootTable, location, false);
    }

    public LootTableHolder(LootTable lootTable, ResourceLocation location, boolean isNew) {
        this.lootTable = lootTable;
        this.location = location;
        this.isNew = isNew;
    }

    public ResourceLocation getLocation() {
        return location;
    }

    public MutableLootTable mutate() {
        if (mutableLootTable == null) {
            mutableLootTable = new MutableLootTable(lootTable, location);
        }

        return mutableLootTable;
    }

    public void ifChanged(Consumer<MutableLootTable> onIfChanged) {
        if (mutableLootTable != null) {
            onIfChanged.accept(mutableLootTable);
        }
    }

    public boolean isChanged() {
        return mutableLootTable != null;
    }

    public boolean isNew() {
        return isNew;
    }

    public void modify(Consumer<MutableLootTable> onModify) {
        onModify.accept(mutate());
    }

    public void clear() {
        mutate().clear();
    }
}
