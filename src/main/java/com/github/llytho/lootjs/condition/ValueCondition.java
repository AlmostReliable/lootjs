package com.github.llytho.lootjs.condition;

import com.github.llytho.lootjs.core.ICondition;
import net.minecraft.loot.LootContext;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public abstract class ValueCondition<V, T> implements Predicate<LootContext> {

    private final V[] values;
    private final Predicate<T> condition;

    public ValueCondition(V[] pValues, ICondition<V, T> pCondition) {
        this.values = pValues;
        this.condition = pCondition.create(pValues, this::match);
    }

    protected abstract boolean match(T t, V v);

    @Nullable
    protected abstract T getValue(LootContext pContext);

    @Override
    public final boolean test(LootContext pContext) {
        T value = getValue(pContext);
        if (value == null) {
            return false;
        }

        return condition.test(value);
    }
}
