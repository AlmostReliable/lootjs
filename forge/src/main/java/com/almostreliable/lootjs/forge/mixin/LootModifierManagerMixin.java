package com.almostreliable.lootjs.forge.mixin;

import com.almostreliable.lootjs.LootModificationsAPI;
import com.almostreliable.lootjs.forge.kube.LootModificationForgeEventJS;
import com.almostreliable.lootjs.kube.LootJSEvent;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifierManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Set;

@Mixin(LootModifierManager.class)
public class LootModifierManagerMixin {

    @Shadow private Map<ResourceLocation, IGlobalLootModifier> registeredLootModifiers;

    @Inject(method = "apply", at = @At("RETURN"), remap = false)
    private void lootModifierReload(Map<ResourceLocation, JsonElement> resourceList, ResourceManager resourceManagerIn, ProfilerFiller profilerIn, CallbackInfo ci) {
        Set<ResourceLocation> locations = this.registeredLootModifiers.keySet();
        LootModificationsAPI.reload();
        LootJSEvent.MODIFIERS.post(new LootModificationForgeEventJS(locations, registeredLootModifiers::remove));
    }
}
