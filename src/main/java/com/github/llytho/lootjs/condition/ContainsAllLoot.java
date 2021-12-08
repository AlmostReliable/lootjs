package com.github.llytho.lootjs.condition;

import net.minecraft.item.ItemStack;

import java.util.List;

public class ContainsAllLoot extends ContainsAnyLoot {

    public ContainsAllLoot(ItemStack[] pItemStacks) {
        super(pItemStacks);
    }

    protected boolean doTest(List<ItemStack> pLoot) {
        for (ItemStack loot : pLoot) {
            for (ItemStack itemStack : items) {
                if (!ItemStack.matches(loot, itemStack)) {
                    return false;
                }
            }
        }

        return true;
    }
}
