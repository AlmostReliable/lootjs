package com.almostreliable.lootjs.util;

import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface NullableFunction<T, R> {

    @Nullable
    R apply(T t);
}
