package com.github.llytho.lootjs.action;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.IAction;
import com.github.llytho.lootjs.core.ILootContextData;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;

import java.util.function.Predicate;

public class RemoveLootAction implements IAction<LootContext> {
    private final Predicate<ItemStack> predicate;

    public RemoveLootAction(Predicate<ItemStack> pPredicate) {
        predicate = pPredicate;
    }

    @Override
    public boolean accept(LootContext pContext) {
        ILootContextData data = pContext.getParamOrNull(Constants.DATA);
        if (data != null) {
            data.getGeneratedLoot().removeIf(predicate);
        }

        return true;
    }
}
