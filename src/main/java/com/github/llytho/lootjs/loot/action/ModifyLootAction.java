package com.github.llytho.lootjs.loot.action;

import com.github.llytho.lootjs.core.ILootAction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ModifyLootAction implements ILootAction {
    private final Predicate<ItemStack> predicate;
    private final Function<ItemStack, ItemStack> itemStack;

    public ModifyLootAction(Predicate<ItemStack> predicate, Function<ItemStack, ItemStack> itemStack) {
        this.predicate = predicate;
        this.itemStack = itemStack;
    }

    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        for (int i = 0; i < loot.size(); i++) {
            if (predicate.test(loot.get(i))) {
                ItemStack currentItemStack = loot.get(i);
                loot.set(i, itemStack.apply(currentItemStack).copy());
            }
        }

        return true;
    }
}
