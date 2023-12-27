package com.almostreliable.lootjs.loot.modifier.handler;

import com.almostreliable.lootjs.loot.modifier.LootHandler;
import com.almostreliable.lootjs.core.LootBucket;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record VanillaConditionWrapper(LootItemCondition condition) implements LootHandler {

    @Override
    public boolean apply(LootContext context, LootBucket loot) {
        return condition.test(context);
    }
}
