package com.almostreliable.lootjs.loot.table.entry;

import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.loot.LootConditionList;
import com.almostreliable.lootjs.util.DebugInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.entries.CompositeEntryBase;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;

import java.util.function.Consumer;

@SuppressWarnings("UnusedReturnValue")
public interface LootContainer {

    static LootContainer ofVanilla(LootPoolEntryContainer entry) {
        if (entry instanceof CompositeEntryBase c) {
            return new CompositeLootEntry(c);
        }

        if (entry instanceof LootPoolSingletonContainer s) {
            return new LootEntry(s);
        }

        return new Unknown(entry);
    }

    LootPoolEntryType getVanillaType();

    LootPoolEntryContainer saveAndGetOrigin();

    LootContainer when(Consumer<LootConditionList> callback);

    boolean test(ItemFilter filter);

    default ResourceLocation getType() {
        ResourceLocation key = BuiltInRegistries.LOOT_POOL_ENTRY_TYPE.getKey(getVanillaType());
        if (key == null) {
            throw new IllegalStateException("Could not find key for loot pool entry type " + getVanillaType());
        }

        return key;
    }

    void collectDebugInfo(DebugInfo info);

    boolean isComposite();

    record Unknown(LootPoolEntryContainer saveAndGetOrigin) implements LootContainer {

        @Override
        public LootPoolEntryType getVanillaType() {
            return saveAndGetOrigin().getType();
        }

        @Override
        public LootContainer when(Consumer<LootConditionList> callback) {
            return this;
        }

        @Override
        public boolean test(ItemFilter filter) {
            return false;
        }

        @Override
        public void collectDebugInfo(DebugInfo info) {
            info.add("Unknown entry type: " + getType());
        }

        @Override
        public boolean isComposite() {
            return false;
        }
    }
}
