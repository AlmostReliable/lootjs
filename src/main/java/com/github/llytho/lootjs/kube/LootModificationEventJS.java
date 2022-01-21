package com.github.llytho.lootjs.kube;

import com.github.llytho.lootjs.LootModificationsAPI;
import com.github.llytho.lootjs.core.*;
import com.github.llytho.lootjs.kube.builder.LootActionsBuilderJS;
import com.github.llytho.lootjs.util.Utils;
import dev.latvian.kubejs.CommonProperties;
import dev.latvian.kubejs.block.BlockStatePredicate;
import dev.latvian.kubejs.event.EventJS;
import dev.latvian.kubejs.script.ScriptType;
import dev.latvian.kubejs.server.ServerJS;
import dev.latvian.kubejs.util.ConsoleJS;
import dev.latvian.kubejs.util.UtilsJS;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LootModificationEventJS extends EventJS {
    private static final Logger LOGGER = LogManager.getLogger();

    private final List<Supplier<ILootModification>> modifierSuppliers = new ArrayList<>();
    private final List<ResourceLocation> originalLocations;
    private final Set<ResourceLocation> locationsToRemove = new HashSet<>();

    public LootModificationEventJS(ArrayList<ResourceLocation> originalLocations) {
        this.originalLocations = originalLocations;
    }

    public List<String> getGlobalModifiers() {
        return originalLocations.stream().map(ResourceLocation::toString).collect(Collectors.toList());
    }

    public void disableLootModification(String... idOrPattern) {
        if (idOrPattern.length == 0) {
            throw new IllegalArgumentException("No loot table were given.");
        }

        List<Pattern> patterns = new ArrayList<>();
        List<ResourceLocation> locations = new ArrayList<>();
        splitLocationsOrPattern(idOrPattern, patterns, locations);

        patterns.forEach(LootModificationsAPI.FILTER::add);
        locations.forEach(LootModificationsAPI.FILTER::add);
    }

    public void enableLogging() {
        LootModificationsAPI.DEBUG_STACK_ENABLED = true;
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

    public LootActionsBuilderJS addLootTableModifier(String... idOrPattern) {
        if (idOrPattern.length == 0) {
            throw new IllegalArgumentException("No loot table were given.");
        }

        List<Pattern> patterns = new ArrayList<>();
        List<ResourceLocation> locations = new ArrayList<>();
        splitLocationsOrPattern(idOrPattern, patterns, locations);

        LootActionsBuilderJS builder = new LootActionsBuilderJS();
        modifierSuppliers.add(() -> {
            List<ILootAction> actions = builder.getActions();
            String logName = builder.getLogName(Utils.quote("LootTables", Arrays.asList(idOrPattern)));
            return new LootModificationByTable(logName,
                    new ArrayList<>(locations),
                    new ArrayList<>(patterns),
                    new ArrayList<>(actions));
        });
        return builder;
    }

    public LootActionsBuilderJS addLootTypeModifier(LootContextType... types) {
        if (types.length == 0) {
            throw new IllegalArgumentException("No loot type were given.");
        }

        LootActionsBuilderJS builder = new LootActionsBuilderJS();
        modifierSuppliers.add(() -> {
            List<ILootAction> actions = builder.getActions();
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
                .filter(block -> !BlockStatePredicate.AIR_ID.equals(block.getRegistryName()))
                .forEach(set::add);

        if (set.isEmpty()) {
            throw new IllegalArgumentException("No valid block given.");
        }

        LootActionsBuilderJS builder = new LootActionsBuilderJS();
        modifierSuppliers.add(() -> {
            List<ILootAction> actions = builder.getActions();
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
            List<ILootAction> actions = builder.getActions();
            String logName = builder.getLogName(Utils.quote("Entities",
                    set.stream().map(ForgeRegistryEntry::getRegistryName).collect(Collectors.toList())));
            return new LootModificationByEntity(logName, set, new ArrayList<>(actions));
        });
        return builder;
    }

    @Override
    protected void afterPosted(boolean result) {
        super.afterPosted(result);

        try {
            for (Supplier<ILootModification> modifierSupplier : modifierSuppliers) {
                LootModificationsAPI.addModification(modifierSupplier.get());
            }
        } catch (Exception exception) {
            ConsoleJS.SERVER.error(exception);
        } finally {
            if (CommonProperties.get().announceReload && ServerJS.instance != null &&
                !CommonProperties.get().hideServerScriptErrors) {
                if (!ScriptType.SERVER.errors.isEmpty()) {
                    ServerJS.instance.tell(new StringTextComponent(
                            "LootJS Errors found! [" + ScriptType.SERVER.errors.size() +
                            "]! Run '/kubejs errors' for more info").withStyle(TextFormatting.DARK_RED));
                }
            }
        }

        originalLocations.removeIf(locationsToRemove::contains);


    }

    public void splitLocationsOrPattern(String[] locationsOrPattern, List<Pattern> patterns, List<ResourceLocation> locations) {
        for (String str : locationsOrPattern) {
            Pattern pattern = UtilsJS.parseRegex(str);
            if (pattern == null) {
                locations.add(new ResourceLocation(str));
            } else {
                patterns.add(pattern);
            }
        }
    }
}
