package com.almostreliable.lootjs.mixin;

import com.almostreliable.lootjs.LootModificationsAPI;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Creeper.class)
public class CreeperMixin {
    @Inject(method = "dropCustomDeathLoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Creeper;increaseDroppedSkulls()V"), cancellable = true)
    private void modifyWitherBossStarDrop(DamageSource damageSource, int i, boolean bl, CallbackInfo ci) {
        if (LootModificationsAPI.DISABLE_CREEPER_DROPPING_HEAD) {
            ci.cancel();
        }
    }
}
