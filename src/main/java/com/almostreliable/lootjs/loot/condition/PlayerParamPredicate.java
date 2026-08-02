package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.LootJSConditions;
import com.almostreliable.lootjs.util.LootContextUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.function.Predicate;

public record PlayerParamPredicate(Predicate<ServerPlayer> predicate) implements LootItemCondition {

    @Override
    public boolean test(LootContext lootContext) {
        ServerPlayer player = LootContextUtils.getPlayerOrNull(lootContext);
        return player != null && predicate.test(player);
    }

    @Override
    public MapCodec<? extends LootItemCondition> codec() {
        return LootJSConditions.PLAYER_PARAM.value();
    }
}
