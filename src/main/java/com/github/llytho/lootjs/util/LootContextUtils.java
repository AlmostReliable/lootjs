package com.github.llytho.lootjs.util;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootContextData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import javax.annotation.Nullable;

public class LootContextUtils {
    @Nullable
    public static ServerPlayer getPlayerOrNull(LootContext context) {
        ILootContextData data = context.getParamOrNull(Constants.DATA);
        if (data == null) return null;

        switch (data.getLootContextType()) {
            case BLOCK:
            case CHEST:
                return tryGetPlayer(context.getParamOrNull(LootContextParams.THIS_ENTITY));
            case ENTITY:
                ServerPlayer player = tryGetPlayer(context.getParamOrNull(LootContextParams.KILLER_ENTITY));
                if (player != null) {
                    return player;
                }

                return tryGetPlayer(context.getParamOrNull(LootContextParams.LAST_DAMAGE_PLAYER));
            case FISHING:
                return tryGetPlayer(context.getParamOrNull(LootContextParams.KILLER_ENTITY));
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
