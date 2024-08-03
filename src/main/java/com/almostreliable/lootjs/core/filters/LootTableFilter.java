package com.almostreliable.lootjs.core.filters;

import com.almostreliable.lootjs.core.LootType;
import com.almostreliable.lootjs.loot.extension.LootContextExtension;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;

public interface LootTableFilter {

    boolean test(LootTable table);

    boolean test(LootContext context);

    record ByIdFilter(IdFilter filter) implements LootTableFilter {

        @Override
        public boolean test(LootTable table) {
            return filter.test(table.getLootTableId());
        }

        @Override
        public boolean test(LootContext context) {
            return filter.test(context.getQueriedLootTableId());
        }
    }

    record ByLootType(LootType type) implements LootTableFilter {

        @Override
        public boolean test(LootTable lootTable) {
            return type == LootType.getLootType(lootTable.getParamSet());
        }

        @Override
        public boolean test(LootContext context) {
            return LootContextExtension.cast(context).lootjs$isType(type);
        }
    }
}
