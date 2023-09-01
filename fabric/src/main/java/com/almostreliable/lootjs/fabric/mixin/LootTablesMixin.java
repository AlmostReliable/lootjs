package com.almostreliable.lootjs.fabric.mixin;

import com.almostreliable.lootjs.LootModificationsAPI;
import com.almostreliable.lootjs.fabric.kubejs.LootModificationFabricEventJS;
import com.almostreliable.lootjs.kube.LootJSEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootDataType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = LootDataManager.class, priority = 9999)
public class LootTablesMixin {

    @Inject(method = "apply", at = @At("RETURN"))
    private void lootjs$lootModifierReload(Map<LootDataType<?>, Map<ResourceLocation, ?>> map, CallbackInfo ci) {
        LootModificationsAPI.reload();
        LootJSEvent.MODIFIERS.post(new LootModificationFabricEventJS());
    }
}
