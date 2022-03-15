package com.github.llytho.lootjs.mixin;

import com.github.llytho.lootjs.LootContextData;
import com.github.llytho.lootjs.LootModificationsAPI;
import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.LootContextType;
import com.github.llytho.lootjs.loot.results.LootInfoCollector;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootContext.Builder.class)
public abstract class LootContextBuilderMixin {
    @Shadow
    public abstract <T> LootContext.Builder withParameter(LootContextParam<T> param, T value);

    @Inject(method = "create", at = @At("HEAD"))
    public void addTypeOnCreate(LootContextParamSet paramSet, CallbackInfoReturnable<LootContext> cir) {
        LootContextType type = Constants.PSETS_TO_TYPE.getOrDefault(paramSet, LootContextType.UNKNOWN);
        this.withParameter(Constants.DATA, new LootContextData(type));

        if (LootModificationsAPI.LOOT_MODIFICATION_LOGGING) {
            this.withParameter(Constants.RESULT_COLLECTOR, new LootInfoCollector());
        }
    }
}
