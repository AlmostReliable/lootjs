package com.github.llytho.lootjs.util;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootAction;
import com.github.llytho.lootjs.core.ILootContextData;
import com.github.llytho.lootjs.core.LootModificationDebug;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;

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

    public static void popResultLayer(LootContext context) {
        LootModificationDebug result = context.getParamOrNull(Constants.RESULT_LOGGER);
        if (result == null) return;
        result.popLayer();
    }


    public static void pushResultLayer(LootContext context) {
        LootModificationDebug result = context.getParamOrNull(Constants.RESULT_LOGGER);
        if (result == null) return;
        result.pushLayer();
    }

    public static void writeConditionInLayer(LootContext context, boolean succeed, ILootCondition condition) {
        LootModificationDebug result = context.getParamOrNull(Constants.RESULT_LOGGER);
        if (result == null) return;

        result.pushLayer();
        String conName = condition.getClass().getName();
        result.write(succeed, "[C] " + conName.substring(conName.lastIndexOf('.') + 1));
        result.popLayer();
    }

    public static boolean writeActionInLayer(LootContext context, boolean succeed, ILootAction action) {
        LootModificationDebug result = context.getParamOrNull(Constants.RESULT_LOGGER);
        if (result == null) return succeed;

        result.pushLayer();
        String actionName = action.getClass().getName();
        result.write(succeed, "[A] " + actionName.substring(actionName.lastIndexOf('.') + 1));
        result.popLayer();
        return succeed;
    }

    @Nullable
    private static ServerPlayerEntity tryGetPlayer(@Nullable Entity entity) {
        if (entity instanceof ServerPlayerEntity) {
            return (ServerPlayerEntity) entity;
        }

        return null;
    }
}
