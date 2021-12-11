package com.github.llytho.lootjs.action;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.IAction;
import com.github.llytho.lootjs.core.ILootContextData;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;

import java.util.function.Predicate;

public class RemoveLootAction implements IAction<LootContext> {
    private final Predicate<ItemStack> predicate;

    public RemoveLootAction(Predicate<ItemStack> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean accept(LootContext context) {
        ILootContextData data = context.getParamOrNull(Constants.DATA);
        if (data != null) {
            data.getGeneratedLoot().removeIf(predicate);
        }

        return true;
    }
}
