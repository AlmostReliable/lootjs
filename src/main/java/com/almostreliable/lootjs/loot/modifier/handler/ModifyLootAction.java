package com.almostreliable.lootjs.loot.modifier.handler;

import com.almostreliable.lootjs.core.LootBucket;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.loot.modifier.LootAction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public record ModifyLootAction(ItemFilter predicate,
                               ModifyLootAction.Callback callback)
        implements LootAction {

    public void apply(LootContext context, LootBucket loot) {
        loot.modifyItems(itemStack -> {
            if (predicate.test(itemStack)) {
                return callback.modify(itemStack);
            }

            return itemStack;
        });
    }

    @FunctionalInterface
    public interface Callback {
        ItemStack modify(ItemStack itemStack);
    }
}
