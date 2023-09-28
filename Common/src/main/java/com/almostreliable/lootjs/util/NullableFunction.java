package com.almostreliable.lootjs.util;

import javax.annotation.Nullable;

@FunctionalInterface
public interface NullableFunction<T, R> {

    @Nullable
    R apply(T t);
}
