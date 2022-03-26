package com.github.llytho.lootjs.loot.action;

import com.github.llytho.lootjs.core.ILootAction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;
import java.util.function.Predicate;

public class ModifyLootAction implements ILootAction {
    private final Predicate<ItemStack> predicate;
    private final Callback callback;

    public ModifyLootAction(Predicate<ItemStack> predicate, Callback callback) {
        this.predicate = predicate;
        this.callback = callback;
    }

    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        for (int i = 0; i < loot.size(); i++) {
            if (predicate.test(loot.get(i))) {
                ItemStack currentItemStack = loot.get(i);
                loot.set(i, callback.modify(currentItemStack).copy());
            }
        }

        return true;
    }

    @FunctionalInterface
    public interface Callback {
        ItemStack modify(ItemStack itemStack);
    }
}
