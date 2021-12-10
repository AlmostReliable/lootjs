package com.github.llytho.lootjs.condition;

import net.minecraft.loot.LootContext;

import javax.annotation.Nullable;
import java.util.Collection;

public abstract class ValueCondition<I, V> implements IExtendedLootCondition {

    private final IConditionOp.Predicate<I, V> predicate;

    public ValueCondition(IConditionOp.Factory<I, V> pFunc) {
        predicate = pFunc.apply(this::match);
    }

    protected abstract boolean match(I i, V v);

    @Nullable
    protected abstract Collection<I> getIterableValue(LootContext pContext);

    @Nullable
    protected abstract V getValue(LootContext pContext);

    @Override
    public final boolean test(LootContext pContext) {
        Collection<I> iterableValue = getIterableValue(pContext);
        V toCheckValue = getValue(pContext);
        if (iterableValue == null || toCheckValue == null) {
            return false;
        }

        return predicate.test(iterableValue, toCheckValue);
    }
}
