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

//    public static void popDebugLayer(LootContext context) {
//        DebugStack result = context.getParamOrNull(Constants.RESULT_LOGGER);
//        if (result == null) return;
//        result.popLayer();
//    }
//
//
//    public static void pushDebugLayer(LootContext context) {
//        DebugStack result = context.getParamOrNull(Constants.RESULT_LOGGER);
//        if (result == null) return;
//        result.pushLayer();
//    }
//
//    public static void writeDebug(LootContext context, String text) {
//        DebugStack result = context.getParamOrNull(Constants.RESULT_LOGGER);
//        if (result == null) return;
//
////        result.pushLayer();
//        result.write(text);
////        result.popLayer();
//    }
//
//    public static void writeDebug(LootContext context, String prefix, boolean succeed, ILootCondition condition) {
//        DebugStack result = context.getParamOrNull(Constants.RESULT_LOGGER);
//        if (result == null) return;
//
//        result.pushLayer();
//        result.write(succeed, "[C] " + prefix + Utils.getClassNameEnding(condition));
//        result.popLayer();
//    }
//
//    public static boolean writeDebug(LootContext context, boolean succeed, ILootAction action) {
//        DebugStack result = context.getParamOrNull(Constants.RESULT_LOGGER);
//        if (result == null) return succeed;
//
//        result.pushLayer();
//        result.write(succeed, "[A] " + Utils.getClassNameEnding(action));
//        result.popLayer();
//        return succeed;
//    }

    @Nullable
    private static ServerPlayerEntity tryGetPlayer(@Nullable Entity entity) {
        if (entity instanceof ServerPlayerEntity) {
            return (ServerPlayerEntity) entity;
        }

        return null;
    }
}
