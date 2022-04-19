package com.github.llytho.lootjs.loot.action;

import com.github.llytho.lootjs.core.ILootAction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;
import java.util.function.Predicate;

public class ReplaceLootAction implements ILootAction {
    private final Predicate<ItemStack> predicate;
    private final ItemStack itemStack;

    public ReplaceLootAction(Predicate<ItemStack> predicate, ItemStack itemStack) {
        this.predicate = predicate;
        this.itemStack = itemStack;
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        for (int i = 0; i < loot.size(); i++) {
            if (predicate.test(loot.get(i))) {
                loot.set(i, itemStack.copy());
            }
        }

        return true;
    }
}
