package com.almostreliable.lootjs.mixin.forge;

import com.almostreliable.lootjs.LootModificationsAPI;
import com.almostreliable.lootjs.kube.LootJSEvent;
import com.almostreliable.lootjs.kube.LootModificationEventJS;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifierManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Mixin(LootModifierManager.class)
public class LootModifierManagerMixin {

    @Shadow(remap = false) private Map<ResourceLocation, IGlobalLootModifier> registeredLootModifiers;

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("RETURN"), remap = false)
    private void lootjs$lootModifierReload(Map<ResourceLocation, JsonElement> resourceList, ResourceManager resourceManagerIn, ProfilerFiller profilerIn, CallbackInfo ci) {
        Set<ResourceLocation> locations = this.registeredLootModifiers.keySet();
        LootModificationsAPI.reload();

        Map<ResourceLocation, IGlobalLootModifier> modifiers = new HashMap<>(registeredLootModifiers);
        LootJSEvent.MODIFIERS.post(new LootModificationEventJS(modifiers));
        registeredLootModifiers = ImmutableMap.copyOf(modifiers);
    }
}
