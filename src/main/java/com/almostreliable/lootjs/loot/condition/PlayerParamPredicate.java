package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.LootJSConditions;
import com.almostreliable.lootjs.util.LootContextUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Objects;
import java.util.function.Predicate;

public class PlayerParamPredicate implements LootItemCondition {
    private final Predicate<ServerPlayer> predicate;

    public PlayerParamPredicate(Predicate<ServerPlayer> predicate) {
        Objects.requireNonNull(predicate);
        this.predicate = predicate;
    }

    @Override
    public boolean test(LootContext lootContext) {
        ServerPlayer player = LootContextUtils.getPlayerOrNull(lootContext);
        return player != null && predicate.test(player);
    }


    @Override
    public LootItemConditionType getType() {
        return LootJSConditions.PLAYER_PARAM.value();
    }
}
