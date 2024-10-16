package com.almostreliable.lootjs.loot.modifier.handler;

import com.almostreliable.lootjs.core.LootBucket;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.loot.modifier.LootAction;
import net.minecraft.world.level.storage.loot.LootContext;

public class RemoveLootAction implements LootAction {
    private final ItemFilter filter;

    public RemoveLootAction(ItemFilter filter) {
        this.filter = filter;
    }

    @Override
    public void apply(LootContext context, LootBucket loot) {
        loot.remove(filter);
    }
}
