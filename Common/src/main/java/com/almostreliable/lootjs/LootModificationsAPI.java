package com.almostreliable.lootjs;

import com.almostreliable.lootjs.core.LootJSParamSets;
import com.almostreliable.lootjs.core.filters.ResourceLocationFilter;
import com.almostreliable.lootjs.loot.modifier.LootModifier;
import com.almostreliable.lootjs.loot.results.LootContextInfo;
import com.almostreliable.lootjs.loot.results.LootInfoCollector;
import com.almostreliable.lootjs.core.LootBucket;
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

    public static final List<ResourceLocationFilter> FILTERS = new ArrayList<>();
	private static final Logger LOGGER = LogManager.getLogger();
    private static final List<LootModifier> modifiers = new ArrayList<>();
    public static Consumer<String> DEBUG_ACTION = LOGGER::info;
    public static boolean LOOT_MODIFICATION_LOGGING = false;
    public static boolean DISABLE_WITHER_DROPPING_NETHER_STAR = false;
    public static boolean DISABLE_ZOMBIE_DROPPING_HEAD = false;
    public static boolean DISABLE_SKELETON_DROPPING_HEAD = false;
    public static boolean DISABLE_CREEPER_DROPPING_HEAD = false;

    private LootModificationsAPI() {
    }

    public static void reload() {
        modifiers.clear();
        LOOT_MODIFICATION_LOGGING = false;
        FILTERS.clear();
        FILTERS.add(new ResourceLocationFilter.ByLocation(new ResourceLocation("minecraft:blocks/fire")));
    }

    public static void invokeActions(List<ItemStack> loot, LootContext context) {
        for (ResourceLocationFilter filter : FILTERS) {
            if (filter.test(LootJSPlatform.INSTANCE.getQueriedLootTableId(context))) {
                return;
            }
        }

        context.getLevel().getProfiler().push("LootModificationsAPI::invokeActions");

        // TODO more testing here. I don't really know why there are empty items in the list or better:
        // TODO There are items which refer to the correct item but their cache flag is true so it acts like air
        loot.removeIf(ItemStack::isEmpty);

        LootContextInfo lootContextInfo = LootContextInfo.create(context, loot);

        LootBucket lootBucket = new LootBucket(context, loot);
        for (var modification : modifiers) {
            modification.run(context, lootBucket);
        }

        handleCollector(context, lootContextInfo, lootBucket);
        context.getLevel().getProfiler().pop();
    }

    private static void handleCollector(LootContext context, @Nullable LootContextInfo lootContextInfo, LootBucket lootBucket) {
        if (DEBUG_ACTION == null || !LOOT_MODIFICATION_LOGGING || lootContextInfo == null) return;

        LootInfoCollector collector = context.getParamOrNull(LootJSParamSets.RESULT_COLLECTOR);
        if (collector == null || collector.getFirstLayer().isEmpty()) return;

        lootContextInfo.updateLoot(lootBucket);

        StringBuilder sb = new StringBuilder()
                .append("\n")
                .append("[ Loot information ]")
                .append("\n");
        lootContextInfo.getCollector().append(sb, 1);
        sb.append("[ Modifications  ]").append("\n");
        collector.append(sb, 1);
        DEBUG_ACTION.accept(sb.toString());
    }


    public static void addModification(LootModifier modifier) {
        modifiers.add(modifier);
    }
}
