package com.github.llytho.lootjs.loot.action;

import com.github.llytho.lootjs.core.ILootAction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

import java.util.List;
import java.util.function.Predicate;

public class LootItemFunctionWrapperAction implements ILootAction {
    public static Predicate<ItemStack> ALWAYS_TRUE = itemStack -> true;

    private final LootItemFunction lootItemFunction;

    /**
     * Can be used to suppress some log warnings from LootItemFunction
     */
    private final Predicate<ItemStack> preFilter;

    public LootItemFunctionWrapperAction(LootItemFunction lootItemFunction) {
        this(lootItemFunction, ALWAYS_TRUE);

    }

    public LootItemFunctionWrapperAction(LootItemFunction lootItemFunction, Predicate<ItemStack> preFilter) {
        this.lootItemFunction = lootItemFunction;
        this.preFilter = preFilter;
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        loot.replaceAll(itemStack -> {
            if (preFilter.test(itemStack)) {
                return lootItemFunction.apply(itemStack, context);
            }
            return itemStack;
        });
        return true;
    }

    public LootItemFunction getLootItemFunction() {
        return lootItemFunction;
    }
}
