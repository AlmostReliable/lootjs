package com.almostreliable.lootjs.util;

import com.almostreliable.lootjs.loot.extension.LootContextExtension;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import javax.annotation.Nullable;

public class LootContextUtils {
    @Nullable
    public static ServerPlayer getPlayerOrNull(LootContext context) {
        switch (LootContextExtension.cast(context).lootjs$getType()) {
            case BLOCK:
            case CHEST:
                return tryGetPlayer(context.getParamOrNull(LootContextParams.THIS_ENTITY));
            case ENTITY:
                ServerPlayer player = tryGetPlayer(context.getParamOrNull(LootContextParams.ATTACKING_ENTITY));
                if (player != null) {
                    return player;
                }

                return tryGetPlayer(context.getParamOrNull(LootContextParams.LAST_DAMAGE_PLAYER));
            case FISHING:
                return tryGetPlayer(context.getParamOrNull(LootContextParams.ATTACKING_ENTITY));
        }

        return null;
    }

    @Nullable
    private static ServerPlayer tryGetPlayer(@Nullable Entity entity) {
        if (entity instanceof ServerPlayer) {
            return (ServerPlayer) entity;
        }

        return null;
    }
}
