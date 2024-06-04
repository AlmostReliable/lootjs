package com.almostreliable.lootjs.loot.modifier;

import com.almostreliable.lootjs.core.LootBucket;
import net.minecraft.world.level.storage.loot.LootContext;

@FunctionalInterface
public interface LootAction {
    void apply(LootContext context, LootBucket loot);
}
