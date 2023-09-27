package com.almostreliable.lootjs.fabric.mixin;

import com.almostreliable.lootjs.fabric.core.FabricLootContextExtension;
import com.almostreliable.lootjs.fabric.kubejs.LootConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootDataResolver;
import net.minecraft.world.level.storage.loot.LootParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LootContext.class)
public class LootContextMixin implements FabricLootContextExtension {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void lootjs$initSetTableId(LootParams lootParams, RandomSource randomSource, LootDataResolver lootDataResolver, CallbackInfo ci) {
        if (lootParams instanceof FabricLootContextExtension lootParamsExt) {
            lootjs$setQueriedLootTableId(lootParamsExt.lootjs$getQueriedLootTableId());
        }
    }

    @Unique
    @Nullable
    private LootConsumer lootjs$lootConsumer;

    @Unique
    private ResourceLocation lootjs$queriedLootTableId;

    public ResourceLocation lootjs$getQueriedLootTableId() {
        return lootjs$queriedLootTableId;
    }

    public void lootjs$setQueriedLootTableId(ResourceLocation id) {
        lootjs$queriedLootTableId = id;
    }

    @Nullable
    @Override
    public LootConsumer lootjs$getLootConsumer() {
        return lootjs$lootConsumer;
    }

    @Override
    public void lootjs$setLootConsumer(LootConsumer consumer) {
        lootjs$lootConsumer = consumer;
    }
}
