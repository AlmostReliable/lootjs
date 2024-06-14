package com.almostreliable.lootjs.mixin;

import com.almostreliable.lootjs.LootModificationsAPI;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
public class ZombieMixin {
    @Inject(method = "dropCustomDeathLoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Creeper;increaseDroppedSkulls()V"), cancellable = true)
    private void modifyWitherBossStarDrop(ServerLevel arg, DamageSource arg2, boolean bl, CallbackInfo ci) {
        if (LootModificationsAPI.DISABLE_ZOMBIE_DROPPING_HEAD) {
            ci.cancel();
        }
    }
}
