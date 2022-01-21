package com.github.llytho.lootjs.kube;

import com.github.llytho.lootjs.LootModificationsAPI;
import com.github.llytho.lootjs.core.*;
import com.github.llytho.lootjs.kube.builder.LootActionsBuilderJS;
import dev.latvian.kubejs.CommonProperties;
import dev.latvian.kubejs.event.EventJS;
import dev.latvian.kubejs.script.ScriptType;
import dev.latvian.kubejs.server.ServerJS;
import dev.latvian.kubejs.util.ConsoleJS;
import dev.latvian.kubejs.util.UtilsJS;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
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

    public List<String> getModifiers() {
        return originalLocations.stream().map(ResourceLocation::toString).collect(Collectors.toList());
    }

    public void enableLogging() {
        LootModificationsAPI.DEBUG_STACK_ENABLED = true;
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

    public LootActionsBuilderJS addModifierForLootTable(String... idOrPattern) {
        if (idOrPattern.length == 0) {
            throw new IllegalArgumentException("No loot table were given.");
        }

        List<Pattern> patterns = new ArrayList<>();
        List<ResourceLocation> locations = new ArrayList<>();

        for (String str : idOrPattern) {
            Pattern pattern = UtilsJS.parseRegex(str);
            if (pattern == null) {
                locations.add(new ResourceLocation(str));
            } else {
                patterns.add(pattern);
            }
        }

        LootActionsBuilderJS builder = new LootActionsBuilderJS();
        modifierSuppliers.add(() -> {
            List<ILootAction> actions = builder.getActions();
            String logName = builder.getLogName("LootTables[" + String.join(",", idOrPattern) + "]");
            return new LootModificationByTable(logName,
                    new ArrayList<>(locations),
                    new ArrayList<>(patterns),
                    new ArrayList<>(actions));
        });
        return builder;
    }

    public LootActionsBuilderJS addModifierForType(LootContextType... types) {
        if (types.length == 0) {
            throw new IllegalArgumentException("No loot type were given.");
        }

        LootActionsBuilderJS builder = new LootActionsBuilderJS();
        modifierSuppliers.add(() -> {
            List<ILootAction> actions = builder.getActions();
            String logName = builder.getLogName(
                    "Types[" + Arrays.stream(types).map(Enum::name).collect(Collectors.joining(",")) + "]");
            return new LootModificationByType(logName, new ArrayList<>(Arrays.asList(types)), new ArrayList<>(actions));
        });
        return builder;
    }

    @Override
    protected void afterPosted(boolean result) {
        super.afterPosted(result);

        try {
            for (Supplier<ILootModification> modifierSupplier : modifierSuppliers) {
                LootModificationsAPI.get().addModification(modifierSupplier.get());
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
}
