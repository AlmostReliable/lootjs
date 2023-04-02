package com.almostreliable.lootjs.forge.kube;

import com.almostreliable.lootjs.kube.LootJSPlugin;
import com.almostreliable.lootjs.kube.LootModificationEventJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.IGlobalLootModifier;

import java.util.*;
import java.util.stream.Collectors;

public class LootModificationForgeEventJS extends LootModificationEventJS {
    private final Map<ResourceLocation, IGlobalLootModifier> modifiers;
    private final List<ResourceLocation> removed = new ArrayList<>();

    public LootModificationForgeEventJS(Map<ResourceLocation, IGlobalLootModifier> modifiers) {
        this.modifiers = modifiers;
    }

    public List<String> getGlobalModifiers() {
        return modifiers.keySet().stream().map(ResourceLocation::toString).collect(Collectors.toList());
    }

    public void removeGlobalModifier(String... locationOrModIds) {
        Set<String> modIds = new HashSet<>();
        Set<ResourceLocation> locations = new HashSet<>();
        for (String locationOrModId : locationOrModIds) {
            if (locationOrModId.startsWith("@")) {
                modIds.add(locationOrModId.substring(1));
            } else {
                locations.add(new ResourceLocation(locationOrModId));
            }
        }

        Set<ResourceLocation> collectedByModIds = modifiers.keySet()
                .stream()
                .filter(rl -> modIds.contains(rl.getNamespace()))
                .collect(Collectors.toSet());
        Set<ResourceLocation> collectedByLocations = modifiers.keySet()
                .stream()
                .filter(locations::contains)
                .collect(Collectors.toSet());

        remove(collectedByModIds);
        remove(collectedByLocations);
    }

    private void remove(Set<ResourceLocation> locations) {
        locations.forEach(modifiers::remove);
        removed.addAll(locations);
    }

    @Override
    protected void afterPosted(boolean result) {
        super.afterPosted(result);

        if (LootJSPlugin.eventsAreDisabled()) {
            return;
        }

        if (!removed.isEmpty()) {
            ConsoleJS.SERVER.info("[LootJS] Removed " + removed.size() + " global loot modifiers: " +
                                  removed.stream().map(ResourceLocation::toString).collect(Collectors.joining(", ")));
        }
    }
}
