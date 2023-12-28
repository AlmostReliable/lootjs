package com.almostreliable.lootjs.loot.modifier.handler;

import com.almostreliable.lootjs.core.LootBucket;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.loot.modifier.LootHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public class ModifyLootAction implements LootHandler {
    private final ItemFilter predicate;
    private final Callback callback;

    public ModifyLootAction(ItemFilter predicate, Callback callback) {
        this.predicate = predicate;
        this.callback = callback;
    }

    public boolean apply(LootContext context, LootBucket loot) {
        loot.modifyItems(itemStack -> {
            if (predicate.test(itemStack)) {
                return callback.modify(itemStack);
            }

            return itemStack;
        });
        return true;
    }

    @FunctionalInterface
    public interface Callback {
        ItemStack modify(ItemStack itemStack);
    }
}
