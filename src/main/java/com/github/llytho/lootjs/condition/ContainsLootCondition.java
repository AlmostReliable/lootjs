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

    public ContainsLootCondition(Predicate<ItemStack> predicate, IConditionOp.Factory<ItemStack, Predicate<ItemStack>> factory) {
        super(factory);
        this.predicate = predicate;
    }

    @Override
    protected boolean match(ItemStack itemStack, Predicate<ItemStack> predicate) {
        return predicate.test(itemStack);
    }

    @Nullable
    @Override
    protected Collection<ItemStack> getLeftIterableValue(LootContext context) {
        ILootContextData data = context.getParamOrNull(Constants.DATA);
        if (data == null) {
            return null;
        }

        return data.getGeneratedLoot();
    }

    @Nullable
    @Override
    protected Predicate<ItemStack> getRightValue(LootContext context) {
        return predicate;
    }
}
