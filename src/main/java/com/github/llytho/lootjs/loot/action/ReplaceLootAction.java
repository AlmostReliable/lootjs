package com.github.llytho.lootjs.loot.action;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.LootAction;
import com.github.llytho.lootjs.core.ILootContextData;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;

import java.util.function.Predicate;

public class ReplaceLootAction implements LootAction {
    private final Predicate<ItemStack> predicate;
    private final ItemStack itemStack;

    public ReplaceLootAction(Predicate<ItemStack> predicate, ItemStack itemStack) {
        this.predicate = predicate;
        this.itemStack = itemStack;
    }

    @Override
    public boolean accept(LootContext context) {
        ILootContextData data = context.getParamOrNull(Constants.DATA);
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
