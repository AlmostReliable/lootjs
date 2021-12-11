package com.github.llytho.lootjs.condition;

import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Function;

public interface IConditionOp {
    interface Factory<I, V> extends Function<Matcher<I, V>, Predicate<I, V>> {}

    interface Predicate<I, V> extends BiPredicate<Collection<I>, V> {
        static <I, T> Predicate<I, T> Or(Matcher<I, T> matcher) {
            return (is, v) -> {
                for (I i : is) {
                    if (matcher.match(i, v)) {
                        return true;
                    }
                }
                return false;
            };
        }

        static <I, T> Predicate<I, T> And(Matcher<I, T> matcher) {
            return (is, v) -> {
                for (I i : is) {
                    if (!matcher.match(i, v)) {
                        return false;
                    }
                }
                return true;
            };
        }
    }

    @FunctionalInterface
    interface Matcher<I, T> {
        boolean match(I pI, T pT);
    }
}
