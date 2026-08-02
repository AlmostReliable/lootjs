package com.almostreliable.lootjs.mixin;

import com.almostreliable.lootjs.LootEvents;
import com.almostreliable.lootjs.LootJS;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = ReloadableServerRegistries.class, priority = 1337)
public class ReloadableServerRegistriesMixin {

    @Inject(method = "createAndValidateFullContext", at = @At("HEAD"))
    private static void lootjs$runLootTableEventJS(LayeredRegistryAccess<RegistryLayer> contextLayers, HolderLookup.Provider contextLookupWithUpdatedTags, List<WritableRegistry<?>> newRegistries, CallbackInfoReturnable<ReloadableServerRegistries.LoadResult> cir) {
        WritableRegistry<LootTable> registry = null;
        try {
            //noinspection unchecked
            registry = (WritableRegistry<LootTable>) newRegistries
                    .stream()
                    .filter(r -> r.key().equals(Registries.LOOT_TABLE))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Loot table registry not found"));
        } catch (Exception e) {
            LootJS.LOG.error("Failed to get loot table registry", e);
        }

        if (registry == null) {
            return;
        }

        LootJS.storeLookup(contextLayers.compositeAccess());
        LootEvents.invoke(registry);
    }
}
