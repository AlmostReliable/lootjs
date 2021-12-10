package com.github.llytho.lootjs.condition;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootContextData;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Predicate;

public class ContainsLootCondition extends ValueCondition<ItemStack, Predicate<ItemStack>> {
    private final Predicate<ItemStack> predicate;

    public ContainsLootCondition(Predicate<ItemStack> pPredicate, IConditionOp.Factory<ItemStack, Predicate<ItemStack>> pFunc) {
        super(pFunc);
        predicate = pPredicate;
    }

    @Override
    protected boolean match(ItemStack itemStack, Predicate<ItemStack> itemStackPredicate) {
        return itemStackPredicate.test(itemStack);
    }

    @Nullable
    @Override
    protected Collection<ItemStack> getIterableValue(LootContext pContext) {
        ILootContextData data = pContext.getParamOrNull(Constants.DATA);
        if (data == null) {
            return null;
        }

        return data.getGeneratedLoot();
    }

    @Nullable
    @Override
    protected Predicate<ItemStack> getValue(LootContext pContext) {
        return predicate;
    }
}
