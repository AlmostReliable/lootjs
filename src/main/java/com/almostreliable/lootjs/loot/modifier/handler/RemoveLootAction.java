package com.almostreliable.lootjs.loot.modifier.handler;

import com.almostreliable.lootjs.core.LootBucket;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.loot.modifier.LootAction;
import net.minecraft.world.level.storage.loot.LootContext;

public record RemoveLootAction(ItemFilter filter) implements LootAction {

    @Override
    public void apply(LootContext context, LootBucket loot) {
        loot.remove(filter);
    }
}
