package com.almostreliable.lootjs.forge.mixin;

import com.almostreliable.lootjs.kube.LootJSEvent;
import com.almostreliable.lootjs.kube.LootTableEventJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootDataType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = LootDataManager.class, priority = 1337)
public class LootDataManagerMixin {


    @Shadow private Map<LootDataId<?>, ?> elements;

    @Inject(method = "apply", at = @At("RETURN"))
    private void lootjs$runLootTableEventJS(Map<LootDataType<?>, Map<ResourceLocation, ?>> map2, CallbackInfo ci) {
        LootTableEventJS event = new LootTableEventJS((LootDataManager)(Object) this);
        LootJSEvent.LOOT_TABLES.post(event);
    }
}
