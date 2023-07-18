package com.almostreliable.lootjs.fabric.mixin;

import com.almostreliable.lootjs.LootModificationsAPI;
import com.almostreliable.lootjs.fabric.LootTableExtension;
import com.almostreliable.lootjs.fabric.kubejs.LootModificationFabricEventJS;
import com.almostreliable.lootjs.kube.LootJSEvent;
import com.google.gson.JsonElement;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = LootTables.class, priority = 9999)
public class LootTablesMixin {

    @Shadow private Map<ResourceLocation, LootTable> tables;

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("RETURN"))
    private void lootjs$lootModifierReload(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
        tables.forEach((id, table) -> {
            ((LootTableExtension) table).lootjs$setLootTableId(id);
        });

        LootModificationsAPI.reload();
        LootJSEvent.MODIFIERS.post(new LootModificationFabricEventJS());
    }
}
