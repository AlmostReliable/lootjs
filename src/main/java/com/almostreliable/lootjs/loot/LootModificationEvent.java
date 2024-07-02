package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.LootModificationsAPI;
import com.almostreliable.lootjs.core.LootType;
import com.almostreliable.lootjs.core.filters.IdFilter;
import com.almostreliable.lootjs.loot.modifier.LootModifier;
import com.almostreliable.lootjs.util.BlockFilter;
import com.almostreliable.lootjs.util.Utils;
import com.google.common.base.Preconditions;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class LootModificationEvent {
    protected final Map<ResourceLocation, IGlobalLootModifier> modifiers;
    protected final List<ResourceLocation> removedGlobalModifiers = new ArrayList<>();
    protected final List<LootModifier.Builder> modifierBuilders = new ArrayList<>();

    public LootModificationEvent(Map<ResourceLocation, IGlobalLootModifier> modifiers) {
        this.modifiers = modifiers;
    }

    public List<String> getGlobalModifiers() {
        return modifiers.keySet().stream().map(ResourceLocation::toString).collect(Collectors.toList());
    }

    public void removeGlobalModifier(IdFilter... filters) {
        Set<ResourceLocation> toRemove = modifiers.keySet().stream().filter(resourceLocation -> {
            for (IdFilter filter : filters) {
                if (filter.test(resourceLocation)) {
                    return true;
                }
            }

            return false;
        }).collect(Collectors.toSet());
        toRemove.forEach(modifiers::remove);
        removedGlobalModifiers.addAll(toRemove);
    }

    public void enableLogging() {
        LootModificationsAPI.DEBUG_LOOT_MODIFIERS = true;
    }

    public void disableLootModification(IdFilter... filters) {
        if (filters.length == 0) {
            throw new IllegalArgumentException("No loot table were given.");
        }

        LootModificationsAPI.FILTERS.addAll(Arrays.asList(filters));
    }

    public LootModifier.Builder addTableModifier(IdFilter... filters) {
        if (filters.length == 0) {
            throw new IllegalArgumentException("No loot table were given.");
        }

        LootModifier.Builder builder = new LootModifier.Builder(new LootModifier.TableFiltered(filters),
                Utils.quote("LootTables", Arrays.asList(filters)));
        modifierBuilders.add(builder);
        return builder;
    }

    public LootModifier.Builder addTypeModifier(LootType... types) {
        if (types.length == 0) {
            throw new IllegalArgumentException("No loot type were given.");
        }

        LootModifier.Builder builder = new LootModifier.Builder(new LootModifier.TypeFiltered(types),
                Utils.quote("Types", Arrays.asList(types)));
        modifierBuilders.add(builder);
        return builder;
    }

    public LootModifier.Builder addBlockModifier(BlockFilter blockFilter) {
        Preconditions.checkNotNull(blockFilter);
        LootModifier.Builder builder = new LootModifier.Builder(new LootModifier.BlockFiltered(blockFilter),
                "BlocksPredicate for: " + StringUtils.abbreviate(blockFilter.toString(), 50));
        modifierBuilders.add(builder);
        return builder;
    }

    public LootModifier.Builder addEntityModifier(HolderSet<EntityType<?>> entities) {
        LootModifier.Builder builder = new LootModifier.Builder(new LootModifier.EntityFiltered(entities),
                entities.toString());
        modifierBuilders.add(builder);
        return builder;
    }

    public void disableWitherStarDrop() {
        LootModificationsAPI.DISABLE_WITHER_DROPPING_NETHER_STAR = true;
    }

    public void disableCreeperHeadDrop() {
        LootModificationsAPI.DISABLE_CREEPER_DROPPING_HEAD = true;
    }

    public void disableSkeletonHeadDrop() {
        LootModificationsAPI.DISABLE_SKELETON_DROPPING_HEAD = true;
    }

    public void disableZombieHeadDrop() {
        LootModificationsAPI.DISABLE_ZOMBIE_DROPPING_HEAD = true;
    }

    @HideFromJS
    public void storeModifiers(Consumer<Throwable> onError) {
        try {
            for (var b : modifierBuilders) {
                LootModificationsAPI.addModification(b.build());
            }
        } catch (Exception exception) {
            onError.accept(exception);
        }
    }
}
