package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.LootModificationsAPI;
import com.almostreliable.lootjs.core.LootType;
import com.almostreliable.lootjs.core.filters.IdFilter;
import com.almostreliable.lootjs.core.filters.LootTableFilter;
import com.almostreliable.lootjs.loot.modifier.LootModifier;
import com.almostreliable.lootjs.util.BlockFilter;
import com.almostreliable.lootjs.util.Utils;
import com.google.common.base.Preconditions;
import dev.latvian.mods.kubejs.script.ConsoleJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class LootModificationEvent {
    protected final Map<Identifier, IGlobalLootModifier> modifiers;
    protected final List<Identifier> removedGlobalModifiers = new ArrayList<>();
    protected final List<LootModifier.Builder> modifierBuilders = new ArrayList<>();

    public LootModificationEvent(Map<Identifier, IGlobalLootModifier> modifiers) {
        this.modifiers = modifiers;
    }

    public List<String> getGlobalModifiers() {
        return modifiers.keySet().stream().map(Identifier::toString).collect(Collectors.toList());
    }

    public void removeGlobalModifiers(IdFilter... filters) {
        Set<Identifier> toRemove = modifiers.keySet().stream().filter(Identifier -> {
            for (IdFilter filter : filters) {
                if (filter.test(Identifier)) {
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

    public LootModifier.Builder addTableModifier(LootTableFilter... filters) {
        if (filters.length == 0) {
            throw new IllegalArgumentException("No loot table were given.");
        }

        LootModifier.Builder builder = new LootModifier.Builder(new LootModifier.TableFiltered(filters),
                Utils.quote("LootTables", Arrays.asList(filters)));
        modifierBuilders.add(builder);
        return builder;
    }

    @Deprecated(forRemoval = true)
    public LootModifier.Builder addTypeModifier(LootType... types) {
        ConsoleJS.SERVER.error("LootJS: `addTypeModifier` is deprecated. Use `addTableModifier` with LootType instead.");
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
