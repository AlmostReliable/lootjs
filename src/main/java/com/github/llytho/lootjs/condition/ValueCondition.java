package com.github.llytho.lootjs.condition;

import net.minecraft.loot.LootContext;

import javax.annotation.Nullable;
import java.util.Collection;

public abstract class ValueCondition<I, V> implements IExtendedLootCondition {

    private final IConditionOp.Predicate<I, V> predicate;

    public ValueCondition(IConditionOp.Factory<I, V> factory) {
        predicate = factory.apply(this::match);
    }

    protected abstract boolean match(I i, V v);

    @Nullable
    protected abstract Collection<I> getLeftIterableValue(LootContext context);

    @Nullable
    protected abstract V getRightValue(LootContext context);

    @Override
    public final boolean test(LootContext context) {
        Collection<I> iterableValue = getLeftIterableValue(context);
        V toCheckValue = getRightValue(context);
        if (iterableValue == null || toCheckValue == null) {
            return false;
        }

        return predicate.test(iterableValue, toCheckValue);
    }
}
