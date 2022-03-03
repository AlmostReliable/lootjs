package com.github.llytho.lootjs.loot.condition;

import com.github.llytho.lootjs.util.LootContextUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Objects;
import java.util.function.BiPredicate;

public class PlayerParamPredicate implements IExtendedLootCondition {
    private final BiPredicate<LootContext, ServerPlayer> predicate;

    public PlayerParamPredicate(BiPredicate<LootContext, ServerPlayer> predicate) {
        Objects.requireNonNull(predicate);
        this.predicate = predicate;
    }

    @Override
    public boolean test(LootContext lootContext) {
        ServerPlayer player = LootContextUtils.getPlayerOrNull(lootContext);
        return player != null && predicate.test(lootContext, player);
    }
}
