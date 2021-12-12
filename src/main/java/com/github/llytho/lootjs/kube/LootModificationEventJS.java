package com.github.llytho.lootjs.kube;

import com.github.llytho.lootjs.LootModificationsAPI;
import com.github.llytho.lootjs.action.CompositeAction;
import dev.latvian.kubejs.event.EventJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.util.ResourceLocation;

import java.util.*;
import java.util.stream.Collectors;

public class LootModificationEventJS extends EventJS {

    private final List<CompositeLootActionBuilder> modifierBuilders = new ArrayList<>();
    private final List<ResourceLocation> originalLocations;
    private final Set<ResourceLocation> locationsToRemove = new HashSet<>();

    public LootModificationEventJS(ArrayList<ResourceLocation> originalLocations) {
        this.originalLocations = originalLocations;
    }

    public List<ResourceLocation> getModifiers() {
        return Collections.unmodifiableList(originalLocations);
    }

    public void removeGlobalLoot(String... locationOrModIds) {
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

    public CompositeLootActionBuilder addModifier() {
        CompositeLootActionBuilder builder = new CompositeLootActionBuilder();
        modifierBuilders.add(builder);
        return builder;
    }

    @HideFromJS
    public List<CompositeLootActionBuilder> getModifierBuilders() {
        return modifierBuilders;
    }

    @Override
    protected void afterPosted(boolean result) {
        super.afterPosted(result);

        List<CompositeAction> actions = getModifierBuilders()
                .stream()
                .map(CompositeLootActionBuilder::build)
                .collect(Collectors.toList());

        for (CompositeAction action : actions) {
            LootModificationsAPI.get().addAction(action);
        }

        originalLocations.removeIf(locationsToRemove::contains);
    }
}
