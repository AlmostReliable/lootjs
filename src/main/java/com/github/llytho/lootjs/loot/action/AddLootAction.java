package com.github.llytho.lootjs.loot.action;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootAction;
import com.github.llytho.lootjs.core.ILootContextData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public class AddLootAction implements ILootAction {

    private final ItemStack[] itemStacks;

    public AddLootAction(ItemStack[] itemStacks) {
        this.itemStacks = itemStacks;
    }

    @Override
    public boolean test(LootContext context) {
        ILootContextData data = context.getParamOrNull(Constants.DATA);
        if (data != null) {
            for (ItemStack itemStack : itemStacks) {
                data.getGeneratedLoot().add(itemStack.copy());
            }
        }
        return true;
    }
}
