package com.github.llytho.lootjs.loot.action;

import com.github.llytho.lootjs.core.ILootAction;
import com.github.llytho.lootjs.core.LootEntry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;

public class AddLootAction implements ILootAction {

    private final LootEntry[] itemStacks;

    public AddLootAction(LootEntry[] itemStacks) {
        this.itemStacks = itemStacks;
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        for (LootEntry itemStack : itemStacks) {
            ItemStack item = itemStack.apply(context);
            loot.add(item);
        }
        return true;
    }
}
