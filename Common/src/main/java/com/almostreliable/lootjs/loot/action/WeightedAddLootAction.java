package com.almostreliable.lootjs.loot.action;

import com.almostreliable.lootjs.core.ILootAction;
import com.almostreliable.lootjs.core.LootEntry;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class WeightedAddLootAction implements ILootAction {

    private final NumberProvider numberProvider;
    private final SimpleWeightedRandomList<LootEntry> weightedRandomList;
    private final boolean allowDuplicateLoot;

    public WeightedAddLootAction(NumberProvider numberProvider, SimpleWeightedRandomList<LootEntry> weightedRandomList, boolean allowDuplicateLoot) {
        this.numberProvider = numberProvider;
        this.weightedRandomList = weightedRandomList;
        this.allowDuplicateLoot = allowDuplicateLoot;
        if (weightedRandomList.isEmpty()) {
            throw new IllegalArgumentException("Weighted list must have at least one item");
        }
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        RandomSource random = context.getLevel().getRandom();
        Collection<ItemStack> rolledItems = allowDuplicateLoot ? new ArrayList<>() : new HashSet<>();
        int lootRolls = numberProvider.getInt(context);
        for (int i = 0; i < lootRolls; i++) {
            weightedRandomList.getRandomValue(random).ifPresent(poolEntry -> {
                ItemStack item = poolEntry.createItem(context);
                if (item != null) {
                    rolledItems.add(item);
                }
            });
        }
        loot.addAll(rolledItems.stream().map(ItemStack::copy).toList());
        return true;
    }
}
