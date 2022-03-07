package com.github.llytho.lootjs.loot.action;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootAction;
import com.github.llytho.lootjs.core.ILootContextData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.function.Function;
import java.util.function.Predicate;

public class ModifyLootAction implements ILootAction {
    private final Predicate<ItemStack> predicate;
    private final Function<ItemStack, ItemStack> itemStack;

    public ModifyLootAction(Predicate<ItemStack> predicate, Function<ItemStack, ItemStack> itemStack) {
        this.predicate = predicate;
        this.itemStack = itemStack;
    }

    @Override
    public boolean test(LootContext context) {
        ILootContextData data = context.getParamOrNull(Constants.DATA);
        if (data != null) {
            for (int i = 0; i < data.getGeneratedLoot().size(); i++) {
                if (predicate.test(data.getGeneratedLoot().get(i))) {
                    ItemStack currentItemStack = data.getGeneratedLoot().get(i);
                    data.getGeneratedLoot().set(i, itemStack.apply(currentItemStack).copy());
                }
            }
        }

        return true;
    }
}
