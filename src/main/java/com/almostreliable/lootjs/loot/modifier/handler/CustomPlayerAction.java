package com.almostreliable.lootjs.loot.modifier.handler;

import com.almostreliable.lootjs.loot.modifier.LootHandler;
import com.almostreliable.lootjs.core.LootBucket;
import com.almostreliable.lootjs.util.LootContextUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Objects;
import java.util.function.Consumer;

public class CustomPlayerAction implements LootHandler {

    private final Consumer<ServerPlayer> action;

    public CustomPlayerAction(Consumer<ServerPlayer> action) {
        Objects.requireNonNull(action);
        this.action = action;
    }

    @Override
    public boolean apply(LootContext context, LootBucket loot) {
        ServerPlayer playerOrNull = LootContextUtils.getPlayerOrNull(context);
        if (playerOrNull != null) {
            action.accept(playerOrNull);
        }
        return true;
    }
}
