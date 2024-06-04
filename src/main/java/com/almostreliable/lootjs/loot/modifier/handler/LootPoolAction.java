package com.almostreliable.lootjs.loot.modifier.handler;

import com.almostreliable.lootjs.core.LootBucket;
import com.almostreliable.lootjs.loot.modifier.LootAction;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;

public record LootPoolAction(LootPool pool) implements LootAction {
    @Override
    public void apply(LootContext context, LootBucket loot) {
        pool.addRandomItems(loot::addItem, context);
    }
}
