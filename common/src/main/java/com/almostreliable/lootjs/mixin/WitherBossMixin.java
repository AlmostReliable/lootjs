package com.almostreliable.lootjs.mixin;

import com.almostreliable.lootjs.LootModificationsAPI;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(WitherBoss.class)
public class WitherBossMixin {

    @ModifyArg(method = "dropCustomDeathLoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/boss/wither/WitherBoss;spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"))
    private ItemLike modifyWitherBossStarDrop(ItemLike item) {
        if (Items.NETHER_STAR == item && LootModificationsAPI.DISABLE_WITHER_DROPPING_NETHER_STAR) {
            return Items.AIR;
        }

        return item;
    }
}
