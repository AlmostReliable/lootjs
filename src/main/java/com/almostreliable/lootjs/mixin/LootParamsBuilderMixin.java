package com.almostreliable.lootjs.mixin;

import com.almostreliable.lootjs.core.LootType;
import com.almostreliable.lootjs.loot.extension.LootParamsExtension;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootParams.Builder.class)
public abstract class LootParamsBuilderMixin {

    @Inject(method = "create", at = @At("RETURN"))
    public void lootjs$setType(LootContextParamSet params, CallbackInfoReturnable<LootParams> cir) {
        LootType type = LootType.getLootType(params);
        ((LootParamsExtension) cir.getReturnValue()).lootjs$setType(type);
    }
}
