package com.almostreliable.lootjs.loot.modifier.handler;

import com.almostreliable.lootjs.loot.modifier.LootHandler;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.core.LootBucket;
import net.minecraft.world.level.storage.loot.LootContext;

public class RemoveLootAction implements LootHandler {
    private final ItemFilter filter;

    public RemoveLootAction(ItemFilter filter) {
        this.filter = filter;
    }

    @Override
    public boolean apply(LootContext context, LootBucket loot) {
        loot.remove(filter);
        return true;
    }
}
