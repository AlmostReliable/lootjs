package com.almostreliable.lootjs.loot.modifier.handler;

import com.almostreliable.lootjs.loot.modifier.LootHandler;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.core.LootBucket;
import com.almostreliable.lootjs.loot.table.entry.LootEntry;
import net.minecraft.world.level.storage.loot.LootContext;

public class ReplaceLootAction implements LootHandler {
    private final ItemFilter filter;
    private final LootEntry lootEntry;
    private final boolean preserveCount;

    public ReplaceLootAction(ItemFilter filter, LootEntry lootEntry, boolean preserveCount) {
        this.filter = filter;
        this.lootEntry = lootEntry;
        this.preserveCount = preserveCount;
    }

    @Override
    public boolean apply(LootContext context, LootBucket loot) {
        loot.replace(filter, lootEntry, preserveCount);
        return true;
    }
}
