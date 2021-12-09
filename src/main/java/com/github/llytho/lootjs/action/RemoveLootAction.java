package com.github.llytho.lootjs.action;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootContextData;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class RemoveLootAction implements Consumer<LootContext> {
    private final Predicate<ItemStack> predicate;

    public RemoveLootAction(Predicate<ItemStack> pPredicate) {
        predicate = pPredicate;
    }

    @Override
    public void accept(LootContext pContext) {
        ILootContextData data = pContext.getParamOrNull(Constants.DATA);
        if (data != null) {
            data.getGeneratedLoot().removeIf(predicate);
        }
    }
}
