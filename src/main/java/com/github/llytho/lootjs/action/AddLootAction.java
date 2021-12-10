package com.github.llytho.lootjs.action;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.IAction;
import com.github.llytho.lootjs.core.ILootContextData;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;

public class AddLootAction implements IAction<LootContext> {

    private final ItemStack[] itemStacks;

    public AddLootAction(ItemStack[] pItemStacks) {
        this.itemStacks = pItemStacks;
    }

    @Override
    public boolean accept(LootContext pContext) {
        ILootContextData data = pContext.getParamOrNull(Constants.DATA);
        if (data != null) {
            for (ItemStack itemStack : itemStacks) {
                data.getGeneratedLoot().add(itemStack.copy());
            }
        }
        return true;
    }
}
