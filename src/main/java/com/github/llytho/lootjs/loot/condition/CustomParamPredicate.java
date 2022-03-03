package com.github.llytho.lootjs.loot.condition;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;

import java.util.Objects;
import java.util.function.BiPredicate;

public class CustomParamPredicate<T> implements IExtendedLootCondition {
    private final BiPredicate<LootContext, T> predicate;
    private final LootContextParam<T> param;

    public CustomParamPredicate(LootContextParam<T> param, BiPredicate<LootContext, T> predicate) {
        Objects.requireNonNull(param);
        Objects.requireNonNull(predicate);
        this.param = param;
        this.predicate = predicate;
    }

    @Override
    public boolean test(LootContext lootContext) {
        T paramOrNull = lootContext.getParamOrNull(param);
        return paramOrNull != null && predicate.test(lootContext, paramOrNull);
    }
}
