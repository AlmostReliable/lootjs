package com.almostreliable.lootjs;

import com.almostreliable.lootjs.core.LootBucket;
import com.almostreliable.lootjs.core.LootContextInfo;
import com.almostreliable.lootjs.core.filters.ResourceLocationFilter;
import com.almostreliable.lootjs.loot.modifier.LootModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LootModificationsAPI {

    public static final List<ResourceLocationFilter> FILTERS = new ArrayList<>();
	private static final Logger LOGGER = LogManager.getLogger();
    private static final List<LootModifier> modifiers = new ArrayList<>();
    public static Consumer<String> DEBUG_ACTION = LOGGER::info;
    public static boolean DISABLE_WITHER_DROPPING_NETHER_STAR = false;
    public static boolean DISABLE_ZOMBIE_DROPPING_HEAD = false;
    public static boolean DISABLE_SKELETON_DROPPING_HEAD = false;
    public static boolean DISABLE_CREEPER_DROPPING_HEAD = false;
    public static boolean DEBUG_LOOT_MODIFIERS = false;

    private LootModificationsAPI() {
    }

    public static void reload() {
        modifiers.clear();
        DEBUG_LOOT_MODIFIERS = false;
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

        LootBucket lootBucket = new LootBucket(context, loot);

        if (isDebug()) {
            LootContextInfo lootContextInfo = LootContextInfo.create(context, lootBucket);
            runModifiers(context, lootBucket);
            lootContextInfo.updateLootAfter(lootBucket);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n");
            lootContextInfo.release(stringBuilder);
            DEBUG_ACTION.accept(stringBuilder.toString());
        } else {
            runModifiers(context, lootBucket);
        }

        context.getLevel().getProfiler().pop();
    }

    private static void runModifiers(LootContext context, LootBucket lootBucket) {
        for (var modification : modifiers) {
            modification.run(context, lootBucket);
        }
    }

    public static void addModification(LootModifier modifier) {
        modifiers.add(modifier);
    }

    public static boolean isDebug() {
        return DEBUG_LOOT_MODIFIERS;
    }
}
