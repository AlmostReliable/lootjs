package com.github.llytho.lootjs.action;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.IAction;
import com.github.llytho.lootjs.core.ILootContextData;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;

import java.util.function.Predicate;

public class ReplaceLootAction implements IAction<LootContext> {
    private final Predicate<ItemStack> predicate;
    private final ItemStack itemStack;

    public ReplaceLootAction(Predicate<ItemStack> pPredicate, ItemStack pItemStack) {
        predicate = pPredicate;
        itemStack = pItemStack;
    }

    @Override
    public boolean accept(LootContext pContext) {
        ILootContextData data = pContext.getParamOrNull(Constants.DATA);
        if (data != null) {
            for (int i = 0; i < data.getGeneratedLoot().size(); i++) {
                if (predicate.test(data.getGeneratedLoot().get(i))) {
                    data.getGeneratedLoot().set(i, itemStack.copy());
                }
            }
        }

        return true;
    }
}
