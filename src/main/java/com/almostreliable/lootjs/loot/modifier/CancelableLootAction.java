package com.almostreliable.lootjs.loot.modifier;

import com.almostreliable.lootjs.core.LootBucket;
import net.minecraft.world.level.storage.loot.LootContext;

import javax.annotation.Nullable;

@FunctionalInterface
public interface CancelableLootAction {

    @Nullable
    Object apply(LootContext context, LootBucket loot);

    static LootHandler asHandler(CancelableLootAction action) {
        return (context, loot) -> {
            var result = action.apply(context, loot);
            return result == null || Boolean.FALSE.equals(result);
        };
    }
}
