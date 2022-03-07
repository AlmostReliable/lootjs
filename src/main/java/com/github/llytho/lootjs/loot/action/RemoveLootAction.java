package com.github.llytho.lootjs.loot.action;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootAction;
import com.github.llytho.lootjs.core.ILootContextData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.function.Predicate;

public class RemoveLootAction implements ILootAction {
    private final Predicate<ItemStack> predicate;

    public RemoveLootAction(Predicate<ItemStack> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(LootContext context) {
        ILootContextData data = context.getParamOrNull(Constants.DATA);
        if (data != null) {
            data.getGeneratedLoot().removeIf(predicate);
        }

        return true;
    }
}
