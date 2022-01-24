package com.github.llytho.lootjs;

import com.github.llytho.lootjs.core.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class LootModificationsAPI {

    public static final ModificationFilter FILTER = new ModificationFilter();
    private static final List<ILootModification> modifications = new ArrayList<>();
    @Nullable
    public static BiConsumer<LootContext, DebugStack> DEBUG_ACTION;
    public static boolean DEBUG_STACK_ENABLED = false;

    private LootModificationsAPI() {
    }

    public static void reload() {
        modifications.clear();
        DEBUG_STACK_ENABLED = false;
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

        DebugStack debug = context.getParamOrNull(Constants.RESULT_LOGGER);

        // TODO more testing here. I don't really know why there are empty items in the list or better:
        // TODO There are items which refer to the correct item but their cache flag is true so it acts like air
        loot.removeIf(ItemStack::isEmpty);
        contextData.setGeneratedLoot(loot);
        for (ILootModification modification : modifications) {
            if (modification.shouldExecute(context)) {
                DebugStack.write(debug, "\uD83D\uDD27 " + modification.getName() + " {");
                modification.execute(context);
                DebugStack.write(debug, "}");
            }
            contextData.reset();
        }

        if (debug != null && DEBUG_ACTION != null) {
            DEBUG_ACTION.accept(context, debug);
        }

        context.getLevel().getProfiler().pop();
    }

    public static void addModification(ILootModification modification) {
        modifications.add(modification);
    }
}
