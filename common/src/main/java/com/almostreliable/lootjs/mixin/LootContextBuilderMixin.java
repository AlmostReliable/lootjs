package com.almostreliable.lootjs.mixin;

import com.almostreliable.lootjs.LootContextData;
import com.almostreliable.lootjs.LootModificationsAPI;
import com.almostreliable.lootjs.core.LootJSParamSets;
import com.almostreliable.lootjs.core.LootContextParamSetsMapping;
import com.almostreliable.lootjs.core.LootContextType;
import com.almostreliable.lootjs.loot.results.LootInfoCollector;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootParams.Builder.class)
public abstract class LootContextBuilderMixin {

    @Shadow
    public abstract <T> LootParams.Builder withParameter(LootContextParam<T> lootContextParam, T object);

    @Inject(method = "create", at = @At("HEAD"))
    public void addTypeOnCreate(LootContextParamSet paramSet, CallbackInfoReturnable<LootContext> cir) {
        LootContextType type = LootContextParamSetsMapping.PSETS_TO_TYPE.getOrDefault(paramSet, LootContextType.UNKNOWN);
        this.withParameter(LootJSParamSets.DATA, new LootContextData(type));

        if (LootModificationsAPI.LOOT_MODIFICATION_LOGGING) {
            this.withParameter(LootJSParamSets.RESULT_COLLECTOR, new LootInfoCollector());
        }
    }
}
