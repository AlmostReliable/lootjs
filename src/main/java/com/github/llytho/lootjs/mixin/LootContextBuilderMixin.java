package com.github.llytho.lootjs.mixin;

import com.github.llytho.lootjs.LootContextData;
import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.LootContextType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameter;
import net.minecraft.loot.LootParameterSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootContext.Builder.class)
public abstract class LootContextBuilderMixin {
    @Shadow
    public abstract <T> LootContext.Builder withParameter(LootParameter<T> pParameter, T pValue);

    @Inject(method = "create", at = @At("HEAD"))
    public void addTypeOnCreate(LootParameterSet pParameterSet, CallbackInfoReturnable<LootContext> pInfo) {
        LootContextType type = Constants.PSETS_TO_TYPE.getOrDefault(pParameterSet, LootContextType.UNKNOWN);
        this.withParameter(Constants.DATA, new LootContextData(type));
    }
}
