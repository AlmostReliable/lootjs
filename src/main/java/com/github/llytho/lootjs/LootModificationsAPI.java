package com.github.llytho.lootjs;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootContextData;
import com.github.llytho.lootjs.core.ILootModification;
import com.github.llytho.lootjs.core.ModificationFilter;
import com.github.llytho.lootjs.loot.results.LootContextInfo;
import com.github.llytho.lootjs.loot.results.LootInfoCollector;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LootModificationsAPI {

    public static final ModificationFilter FILTER = new ModificationFilter();
    private static final Logger LOGGER = LogManager.getLogger();
    private static final List<ILootModification> modifications = new ArrayList<>();
    public static Consumer<String> DEBUG_ACTION = LOGGER::info;
    public static boolean LOOT_MODIFICATION_LOGGING = false;

    private LootModificationsAPI() {
    }

    public static void reload() {
        modifications.clear();
        LOOT_MODIFICATION_LOGGING = false;
        FILTER.clear();
        FILTER.add(new ResourceLocation("minecraft:blocks/fire"));
    }

    public static void invokeActions(List<ItemStack> loot, LootContext context) {
        ILootContextData contextData = context.getParamOrNull(Constants.DATA);
        assert contextData != null;

        if (FILTER.matches(context)) {
            return;
        }

        context.getLevel().getProfiler().push("LootModificationsAPI::invokeActions");

        // TODO more testing here. I don't really know why there are empty items in the list or better:
        // TODO There are items which refer to the correct item but their cache flag is true so it acts like air
        loot.removeIf(ItemStack::isEmpty);
        contextData.setGeneratedLoot(loot);

        LootContextInfo lootContextInfo = LootContextInfo.create(context);

        for (ILootModification modification : modifications) {
            if (modification.shouldExecute(context)) {
                modification.execute(context, loot);
            }
            contextData.reset();
        }

        handleCollector(context, lootContextInfo);
        context.getLevel().getProfiler().pop();
    }

    private static void handleCollector(LootContext context, @Nullable LootContextInfo lootContextInfo) {
        if (DEBUG_ACTION == null || !LOOT_MODIFICATION_LOGGING || lootContextInfo == null) return;

        LootInfoCollector collector = context.getParamOrNull(Constants.RESULT_COLLECTOR);
        ILootContextData data = context.getParamOrNull(Constants.DATA);
        if (collector == null || data == null || collector.getFirstLayer().isEmpty()) return;

        lootContextInfo.updateLoot(data.getGeneratedLoot());

        StringBuilder sb = new StringBuilder()
                .append("\n")
                .append("[ Loot information ]")
                .append("\n");
        lootContextInfo.getCollector().append(sb, 1);
        sb.append("[ Modifications  ]").append("\n");
        collector.append(sb, 1);
        DEBUG_ACTION.accept(sb.toString());
    }


    public static void addModification(ILootModification modification) {
        modifications.add(modification);
    }
}
