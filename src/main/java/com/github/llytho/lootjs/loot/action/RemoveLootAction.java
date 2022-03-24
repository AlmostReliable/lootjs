package com.github.llytho.lootjs.loot.action;

import com.github.llytho.lootjs.core.ILootAction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;
import java.util.function.Predicate;

public class RemoveLootAction implements ILootAction {
    private final Predicate<ItemStack> predicate;

    public RemoveLootAction(Predicate<ItemStack> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        loot.removeIf(predicate);
        return true;
    }
}
