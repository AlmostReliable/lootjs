package com.almostreliable.lootjs.loot.modifier.handler;

import com.almostreliable.lootjs.core.ItemStackFactory;
import com.almostreliable.lootjs.core.LootBucket;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.loot.modifier.LootAction;
import net.minecraft.world.level.storage.loot.LootContext;

public class ReplaceLootAction implements LootAction {
    private final ItemFilter filter;
    private final ItemStackFactory itemStackFactory;
    private final boolean preserveCount;

    public ReplaceLootAction(ItemFilter filter, ItemStackFactory itemStackFactory, boolean preserveCount) {
        this.filter = filter;
        this.itemStackFactory = itemStackFactory;
        this.preserveCount = preserveCount;
    }

    @Override
    public void apply(LootContext context, LootBucket loot) {
        loot.replace(filter, itemStackFactory, preserveCount);
    }
}
