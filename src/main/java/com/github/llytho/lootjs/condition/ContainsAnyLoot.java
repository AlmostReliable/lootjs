package com.github.llytho.lootjs.condition;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootContextData;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;

import java.util.List;
import java.util.function.Predicate;

public class ContainsAnyLoot implements Predicate<LootContext> {

    protected final ItemStack[] items;

    public ContainsAnyLoot(ItemStack[] pItemStacks) {
        this.items = pItemStacks;
        for (ItemStack item : items) {
            if (item.isEmpty()) {
                throw new IllegalArgumentException("Empty item stack found. " + item.getItem().getRegistryName());
            }
        }
    }

    @Override
    public final boolean test(LootContext pContext) {
        ILootContextData data = pContext.getParamOrNull(Constants.DATA);
        if (data == null) {
            return false;
        }

        return doTest(data.getGeneratedLoot());
    }

    protected boolean doTest(List<ItemStack> pLoot) {
        for (ItemStack loot : pLoot) {
            for (ItemStack itemStack : items) {
                if (ItemStack.matches(loot, itemStack)) {
                    return true;
                }
            }
        }
        return false;
    }
}
