package com.github.llytho.lootjs.util;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootContextData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;

import javax.annotation.Nullable;

public class LootContextUtils {
    @Nullable
    public static ServerPlayerEntity getPlayerOrNull(LootContext context) {
        ILootContextData data = context.getParamOrNull(Constants.DATA);
        if (data == null) return null;

        switch (data.getLootContextType()) {
            case BLOCK:
            case CHEST:
                return tryGetPlayer(context.getParamOrNull(LootParameters.THIS_ENTITY));
            case ENTITY:
                return tryGetPlayer(context.getParamOrNull(LootParameters.LAST_DAMAGE_PLAYER));
            case FISHING:
                return tryGetPlayer(context.getParamOrNull(LootParameters.KILLER_ENTITY));
        }

        return null;
    }

    @Nullable
    private static ServerPlayerEntity tryGetPlayer(@Nullable Entity entity) {
        if (entity instanceof ServerPlayerEntity) {
            return (ServerPlayerEntity) entity;
        }

        return null;
    }
}
