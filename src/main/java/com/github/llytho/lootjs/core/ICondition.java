package com.github.llytho.lootjs.core;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

@FunctionalInterface
public interface ICondition<V, T> {

    static <V, T> Predicate<T> Or(V[] pValues, BiPredicate<T, V> pPredicate) {
        return (t) -> {
            for (V v : pValues) {
                if (pPredicate.test(t, v)) {
                    return true;
                }
            }

            return false;
        };
    }

    static <V, T> Predicate<T> And(V[] pValues, BiPredicate<T, V> pPredicate) {
        return (t) -> {
            for (V v : pValues) {
                if (!pPredicate.test(t, v)) {
                    return false;
                }
            }

            return true;
        };
    }

    Predicate<T> create(V[] pValues, BiPredicate<T, V> pPredicate);
}
