package com.almostreliable.lootjs.mixin;

import com.almostreliable.lootjs.core.LootType;
import com.almostreliable.lootjs.loot.extension.LootParamsExtension;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.storage.loot.LootParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootParams.Builder.class)
public abstract class LootParamsBuilderMixin {

    @Inject(method = "create", at = @At("RETURN"))
    public void lootjs$setType(ContextKeySet params, CallbackInfoReturnable<LootParams> cir) {
        LootType type = LootType.getLootType(params);
        ((LootParamsExtension) cir.getReturnValue()).lootjs$setType(type);
    }
}
