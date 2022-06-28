package com.almostreliable.lootjs.kube;

import com.almostreliable.lootjs.LootModificationsAPI;
import com.almostreliable.lootjs.core.*;
import com.almostreliable.lootjs.filters.ResourceLocationFilter;
import com.almostreliable.lootjs.kube.builder.LootActionsBuilderJS;
import com.almostreliable.lootjs.util.Utils;
import dev.latvian.mods.kubejs.CommonProperties;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.server.ServerJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class LootModificationEventJS extends EventJS {

    private final List<Supplier<ILootAction>> modifierSuppliers = new ArrayList<>();

    public void enableLogging() {
        LootModificationsAPI.LOOT_MODIFICATION_LOGGING = true;
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

    public LootActionsBuilderJS addBlockLootModifier(BlockStatePredicate blocks) {
        HashSet<Block> set = new HashSet<>();
        blocks
                .getBlocks()
                .stream()
                .filter(block -> !BlockStatePredicate.AIR_ID.equals(Registry.BLOCK.getKey(block)))
                .forEach(set::add);

        if (set.isEmpty()) {
            throw new IllegalArgumentException("No valid block given.");
        }

        LootActionsBuilderJS builder = new LootActionsBuilderJS();
        modifierSuppliers.add(() -> {
            List<ILootHandler> actions = builder.getHandlers();
            String logName = builder.getLogName(Utils.quote("Blocks", set));
            return new LootModificationByBlock(logName, set, new ArrayList<>(actions));
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
                    set.stream().map(Registry.ENTITY_TYPE::getKey).collect(Collectors.toList())));
            return new LootModificationByEntity(logName, set, new ArrayList<>(actions));
        });
        return builder;
    }

    @Override
    protected void afterPosted(boolean result) {
        super.afterPosted(result);

        if(LootJSPlugin.eventsAreDisabled()) {
            return;
        }

        try {
            for (Supplier<ILootAction> modifierSupplier : modifierSuppliers) {
                LootModificationsAPI.addModification(modifierSupplier.get());
            }
        } catch (Exception exception) {
            ConsoleJS.SERVER.error(exception);
        } finally {
            if (CommonProperties.get().announceReload && ServerJS.instance != null &&
                !CommonProperties.get().hideServerScriptErrors) {
                if (!ScriptType.SERVER.errors.isEmpty()) {
                    ServerJS.instance.tell(new TextComponent(
                            "LootJS Errors found! [" + ScriptType.SERVER.errors.size() +
                            "]! Run '/kubejs errors' for more info").withStyle(ChatFormatting.DARK_RED));
                }
            }
        }
    }
}
