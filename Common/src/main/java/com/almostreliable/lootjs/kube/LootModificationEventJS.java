package com.almostreliable.lootjs.kube;

import com.almostreliable.lootjs.LootModificationsAPI;
import com.almostreliable.lootjs.core.*;
import com.almostreliable.lootjs.filters.ResourceLocationFilter;
import com.almostreliable.lootjs.kube.builder.LootActionsBuilderJS;
import com.almostreliable.lootjs.util.Utils;
import com.google.common.base.Preconditions;
import dev.latvian.mods.kubejs.CommonProperties;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.event.EventResult;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class LootModificationEventJS extends EventJS {

    private final List<Supplier<ILootAction>> modifierSuppliers = new ArrayList<>();

    public void enableLogging() {
        LootModificationsAPI.LOOT_MODIFICATION_LOGGING = true;
    }

    public void disableLootModification(ResourceLocationFilter... filters) {
        if (filters.length == 0) {
            throw new IllegalArgumentException("No loot table were given.");
        }

        LootModificationsAPI.FILTERS.addAll(Arrays.asList(filters));
    }

    public LootActionsBuilderJS addLootTableModifier(ResourceLocationFilter... filters) {
        if (filters.length == 0) {
            throw new IllegalArgumentException("No loot table were given.");
        }

        LootActionsBuilderJS builder = new LootActionsBuilderJS();
        modifierSuppliers.add(() -> {
            List<ILootHandler> actions = builder.getHandlers();
            String logName = builder.getLogName(Utils.quote("LootTables", Arrays.asList(filters)));
            return new LootModificationByTable(logName, filters, new ArrayList<>(actions));
        });
        return builder;
    }

    public LootActionsBuilderJS addLootTypeModifier(LootContextType... types) {
        if (types.length == 0) {
            throw new IllegalArgumentException("No loot type were given.");
        }

        LootActionsBuilderJS builder = new LootActionsBuilderJS();
        modifierSuppliers.add(() -> {
            List<ILootHandler> actions = builder.getHandlers();
            String logName = builder.getLogName(Utils.quote("Types", Arrays.asList(types)));
            return new LootModificationByType(logName, new ArrayList<>(Arrays.asList(types)), new ArrayList<>(actions));
        });
        return builder;
    }

    public LootActionsBuilderJS addBlockLootModifier(Object o) {
        Preconditions.checkNotNull(o);
        BlockStatePredicate blockStatePredicate = BlockStatePredicate.of(o);
        LootActionsBuilderJS builder = new LootActionsBuilderJS();
        modifierSuppliers.add(() -> {
            List<ILootHandler> actions = builder.getHandlers();
            String logName = builder.getLogName("BlocksPredicate for: " + StringUtils.abbreviate(o.toString(), 50));
            return new LootModificationByBlock(logName, blockStatePredicate::test, new ArrayList<>(actions));
        });
        return builder;
    }

    public LootActionsBuilderJS addEntityLootModifier(EntityType<?>... entities) {
        HashSet<EntityType<?>> set = new HashSet<>();
        Arrays.stream(entities).filter(Objects::nonNull).forEach(set::add);

        if (set.isEmpty()) {
            throw new IllegalArgumentException("No valid entities given.");
        }

        LootActionsBuilderJS builder = new LootActionsBuilderJS();
        modifierSuppliers.add(() -> {
            List<ILootHandler> actions = builder.getHandlers();
            String logName = builder.getLogName(Utils.quote("Entities",
                    set.stream().map(BuiltInRegistries.ENTITY_TYPE::getKey).collect(Collectors.toList())));
            return new LootModificationByEntity(logName, set, new ArrayList<>(actions));
        });
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

    @Override
    protected void afterPosted(EventResult result) {
        super.afterPosted(result);

        if (LootJSPlugin.eventsAreDisabled()) {
            return;
        }

        try {
            for (Supplier<ILootAction> modifierSupplier : modifierSuppliers) {
                LootModificationsAPI.addModification(modifierSupplier.get());
            }
        } catch (Exception exception) {
            ConsoleJS.SERVER.error(exception);
        }
    }
}
