package com.github.llytho.lootjs.loot.action;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootAction;
import com.github.llytho.lootjs.loot.results.LootInfoCollector;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

import java.util.List;
import java.util.function.Predicate;

public class LootItemFunctionWrapperAction implements ILootAction {
    private final LootItemFunction lootItemFunction;

    public LootItemFunctionWrapperAction(LootItemFunction lootItemFunction) {
        this.lootItemFunction = lootItemFunction;
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        loot.replaceAll(itemStack -> lootItemFunction.apply(itemStack, context));
        return true;
    }

    public LootItemFunction getLootItemFunction() {
        return lootItemFunction;
    }

    public static class CompositeLootItemFunction implements LootItemFunction {

        private final List<LootItemFunction> lootItemFunctions;
        private final Predicate<ItemStack> filter;

        public CompositeLootItemFunction(List<LootItemFunction> lootItemFunctions, Predicate<ItemStack> filter) {
            this.lootItemFunctions = lootItemFunctions;
            this.filter = filter;
        }

        @Override
        public LootItemFunctionType getType() {
            // TODO check this
            return null;
        }

        @Override
        public ItemStack apply(ItemStack itemStack, LootContext context) {
            if (!filter.test(itemStack)) {
                return itemStack;
            }

            itemStack = itemStack.copy();
            for (LootItemFunction lootItemFunction : lootItemFunctions) {
                itemStack = lootItemFunction.apply(itemStack, context);
            }
            return itemStack;
        }
    }

//    public static class FilteredFunctions implements ILootAction {
//        private final List<LootItemFunction> functions;
//        private final Predicate<ItemStack> filter;
//
//        public FilteredFunctions(List<LootItemFunction> functions, Predicate<ItemStack> filter) {
//            this.functions = functions;
//            this.filter = filter;
//        }
//
//        @Override
//        public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
//            LootInfoCollector collector = context.getParamOrNull(Constants.RESULT_COLLECTOR);
//            for (LootItemFunction function : functions) {
//                LootInfoCollector.createFunctionInfo(collector, function);
//                loot.replaceAll(itemStack -> {
//                    if (filter.test(itemStack)) {
//                        return function.apply(itemStack, context);
//                    }
//                    return itemStack;
//                });
//
//            }
//            return false;
//        }
//    }
}
