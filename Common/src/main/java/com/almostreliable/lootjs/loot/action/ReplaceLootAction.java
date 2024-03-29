package com.almostreliable.lootjs.loot.action;

import com.almostreliable.lootjs.core.ILootAction;
import com.almostreliable.lootjs.core.LootEntry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;
import java.util.function.Predicate;

public class ReplaceLootAction implements ILootAction {
    private final Predicate<ItemStack> predicate;
    private final LootEntry lootEntry;
    private final boolean preserveCount;

    public ReplaceLootAction(Predicate<ItemStack> predicate, LootEntry lootEntry, boolean preserveCount) {
        this.predicate = predicate;
        this.lootEntry = lootEntry;
        this.preserveCount = preserveCount;
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        for (int i = 0; i < loot.size(); i++) {
            ItemStack currentItemStack = loot.get(i);
            if (predicate.test(currentItemStack)) {
                ItemStack newItem = this.lootEntry.createItem(context);
                if (newItem == null) {
                    continue;
                }

                if (preserveCount) {
                    newItem.setCount(Math.min(currentItemStack.getCount(), newItem.getMaxStackSize()));
                }
                loot.set(i, newItem);
            }
        }

        return true;
    }
}
