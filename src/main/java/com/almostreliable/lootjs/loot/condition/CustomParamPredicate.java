package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.LootJSConditions;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Objects;
import java.util.function.Predicate;

public class CustomParamPredicate<T> implements LootItemCondition {
    private final Predicate<T> predicate;
    private final LootContextParam<T> param;

    public CustomParamPredicate(LootContextParam<T> param, Predicate<T> predicate) {
        Objects.requireNonNull(param);
        Objects.requireNonNull(predicate);
        this.param = param;
        this.predicate = predicate;
    }

    @Override
    public boolean test(LootContext lootContext) {
        T paramOrNull = lootContext.getParamOrNull(param);
        return paramOrNull != null && predicate.test(paramOrNull);
    }


    @Override
    public LootItemConditionType getType() {
        return LootJSConditions.PARAM.value();
    }
}
