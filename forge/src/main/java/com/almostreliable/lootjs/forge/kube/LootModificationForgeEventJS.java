package com.almostreliable.lootjs.forge.kube;

import com.almostreliable.lootjs.LootModificationsAPI;
import com.almostreliable.lootjs.filters.ResourceLocationFilter;
import com.almostreliable.lootjs.kube.LootModificationEventJS;
import net.minecraft.resources.ResourceLocation;

import java.util.*;
import java.util.stream.Collectors;

public class LootModificationForgeEventJS extends LootModificationEventJS {
    private final List<ResourceLocation> originalLocations;
    private final Set<ResourceLocation> locationsToRemove = new HashSet<>();

    public LootModificationForgeEventJS(ArrayList<ResourceLocation> originalLocations) {
        this.originalLocations = originalLocations;
    }

    public List<String> getGlobalModifiers() {
        return originalLocations.stream().map(ResourceLocation::toString).collect(Collectors.toList());
    }

    public void disableLootModification(ResourceLocationFilter... filters) {
        if (filters.length == 0) {
            throw new IllegalArgumentException("No loot table were given.");
        }

        LootModificationsAPI.FILTERS.addAll(Arrays.asList(filters));
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

        Set<ResourceLocation> collectedByModIds = originalLocations
                .stream()
                .filter(rl -> modIds.contains(rl.getNamespace()))
                .collect(Collectors.toSet());
        Set<ResourceLocation> collectedByLocations = originalLocations
                .stream()
                .filter(locations::contains)
                .collect(Collectors.toSet());

        locationsToRemove.addAll(collectedByModIds);
        locationsToRemove.addAll(collectedByLocations);
    }

    @Override
    protected void afterPosted(boolean result) {
        super.afterPosted(result);
        originalLocations.removeIf(locationsToRemove::contains);
    }
}
