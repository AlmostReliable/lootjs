package com.almostreliable.lootjs.loot.modifier.handler;

import com.almostreliable.lootjs.core.LootBucket;
import com.almostreliable.lootjs.core.entry.SingleLootEntry;
import com.almostreliable.lootjs.loot.modifier.LootHandler;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class WeightedAddLootAction implements LootHandler {

    private final NumberProvider numberProvider;
    private final SimpleWeightedRandomList<SingleLootEntry> weightedRandomList;
    private final boolean allowDuplicateLoot;

    public WeightedAddLootAction(NumberProvider numberProvider, SimpleWeightedRandomList<SingleLootEntry> weightedRandomList, boolean allowDuplicateLoot) {
        this.numberProvider = numberProvider;
        this.weightedRandomList = weightedRandomList;
        this.allowDuplicateLoot = allowDuplicateLoot;
        if (weightedRandomList.isEmpty()) {
            throw new IllegalArgumentException("Weighted list must have at least one item");
        }
    }

    @Override
    public boolean apply(LootContext context, LootBucket loot) {
        RandomSource random = context.getLevel().getRandom();
        Collection<ItemStack> rolledItems = allowDuplicateLoot ? new ArrayList<>() : new HashSet<>();
        int lootRolls = numberProvider.getInt(context);
        for (int i = 0; i < lootRolls; i++) {
            weightedRandomList.getRandomValue(random).ifPresent(poolEntry -> {
                ItemStack item = poolEntry.create(context);
                if (item != null) {
                    rolledItems.add(item);
                }
            });
        }

        loot.addAllItems(rolledItems.stream().map(ItemStack::copy).toList());
        return true;
    }
}
