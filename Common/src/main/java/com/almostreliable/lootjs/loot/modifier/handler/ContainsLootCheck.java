package com.almostreliable.lootjs.loot.modifier.handler;

import com.almostreliable.lootjs.loot.modifier.LootHandler;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.core.LootBucket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public class ContainsLootCheck implements LootHandler {
    private final ItemFilter itemFilter;
    private final boolean exact;

    public ContainsLootCheck(ItemFilter itemFilter, boolean exact) {
        this.itemFilter = itemFilter;
        this.exact = exact;
    }

    @Override
    public boolean apply(LootContext context, LootBucket loot) {
        return exact ? matchExact(loot) : match(loot);
    }

    private boolean match(LootBucket loot) {
        for (ItemStack itemStack : loot) {
            if (itemFilter.test(itemStack)) {
                return true;
            }
        }

        return false;
    }

    private boolean matchExact(LootBucket loot) {
        for (ItemStack itemStack : loot) {
            if (!itemFilter.test(itemStack)) {
                return false;
            }
        }

        return true;
    }
}
