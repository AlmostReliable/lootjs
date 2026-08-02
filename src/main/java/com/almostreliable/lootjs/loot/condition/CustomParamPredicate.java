package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.LootJSConditions;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.function.Predicate;

public record CustomParamPredicate<T>(ContextKey<T> param, Predicate<T> predicate) implements LootItemCondition {

    @Override
    public boolean test(LootContext lootContext) {
        T paramOrNull = lootContext.getOptionalParameter(param);
        return paramOrNull != null && predicate.test(paramOrNull);
    }

    @Override
    public MapCodec<? extends LootItemCondition> codec() {
        return LootJSConditions.PARAM.value();
    }
}
