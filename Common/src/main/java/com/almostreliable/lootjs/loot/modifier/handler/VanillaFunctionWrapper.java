package com.almostreliable.lootjs.loot.modifier.handler;

import com.almostreliable.lootjs.loot.modifier.LootHandler;
import com.almostreliable.lootjs.core.LootBucket;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

public record VanillaFunctionWrapper(LootItemFunction lootItemFunction) implements LootHandler {

    @Override
    public boolean apply(LootContext context, LootBucket loot) {
        loot.modifyItems(itemStack -> lootItemFunction.apply(itemStack, context));
        return true;
    }
}
