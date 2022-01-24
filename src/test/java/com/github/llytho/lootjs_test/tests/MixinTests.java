package com.github.llytho.lootjs_test.tests;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootContextData;
import com.github.llytho.lootjs.core.LootContextType;
import com.github.llytho.lootjs_test.AllTests;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Objects;

public class MixinTests {
    public static void loadTests() {
        AllTests.add("ParamSet", helper -> {
            LootContext ctx = helper.chestContext(helper.player.position(), true);
            ILootContextData data = ctx.getParamOrNull(Constants.DATA);
            helper.shouldSucceed(data != null, "Data param should always exist");
            helper.shouldSucceed(Objects.requireNonNull(data).getLootContextType() == LootContextType.CHEST,
                    "Context type is CHEST");

            LootContext ctxFromCreeper = helper.entityContext(EntityType.CREEPER, helper.player.position());
            ILootContextData dataFromCreeperCtx = ctxFromCreeper.getParamOrNull(Constants.DATA);
            helper.shouldSucceed(
                    Objects.requireNonNull(dataFromCreeperCtx).getLootContextType() == LootContextType.ENTITY,
                    "Context type is ENTITY");

            LootContext ctxBlock = helper.blockContext(helper.player.blockPosition().below().below(), true);
            ILootContextData ctxBlockData = ctxBlock.getParamOrNull(Constants.DATA);
            helper.shouldSucceed(Objects.requireNonNull(ctxBlockData).getLootContextType() == LootContextType.BLOCK,
                    "Context type is BLOCK");

            LootContext ctxFishing = helper.fishingContext(helper.player.position(), 2);
            ILootContextData ctxFishingData = ctxFishing.getParamOrNull(Constants.DATA);
            helper.shouldSucceed(Objects.requireNonNull(ctxFishingData).getLootContextType() == LootContextType.FISHING,
                    "Context type is FISHING");

            LootContext ctxUnknown = helper.unknownContext(helper.player.position());
            ILootContextData ctxUnknownData = ctxUnknown.getParamOrNull(Constants.DATA);
            helper.shouldSucceed(Objects.requireNonNull(ctxUnknownData).getLootContextType() == LootContextType.UNKNOWN,
                    "Context type is UNKNOWN");
        });
    }
}
