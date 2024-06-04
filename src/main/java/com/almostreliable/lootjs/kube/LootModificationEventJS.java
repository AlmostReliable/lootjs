package com.almostreliable.lootjs.kube;

import com.almostreliable.lootjs.loot.LootModificationEvent;
import dev.latvian.mods.kubejs.event.EventResult;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;

import java.util.Map;
import java.util.stream.Collectors;

public class LootModificationEventJS extends LootModificationEvent implements KubeEvent {

    public LootModificationEventJS(Map<ResourceLocation, IGlobalLootModifier> modifiers) {
        super(modifiers);
    }

    @Override
    public void afterPosted(EventResult result) {
        if (LootJSPlugin.eventsAreDisabled()) {
            return;
        }

        if (!removedGlobalModifiers.isEmpty()) {
            ConsoleJS.SERVER.info("[LootJS] Removed " + removedGlobalModifiers.size() + " global loot modifiers: " +
                                  removedGlobalModifiers
                                          .stream()
                                          .map(ResourceLocation::toString)
                                          .collect(Collectors.joining(", ")));
        }

        storeModifiers(throwable -> ConsoleJS.SERVER.error(throwable));
    }
}
