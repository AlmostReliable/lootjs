package com.github.llytho.lootjs.loot.action;

import com.github.llytho.lootjs.core.ILootAction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;

public class AddLootAction implements ILootAction {

    private final ItemStack[] itemStacks;

    public AddLootAction(ItemStack[] itemStacks) {
        this.itemStacks = itemStacks;
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        for (ItemStack itemStack : itemStacks) {
            loot.add(itemStack.copy());
        }
        return true;
    }
}
