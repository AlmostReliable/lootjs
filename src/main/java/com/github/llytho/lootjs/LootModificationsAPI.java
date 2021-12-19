package com.github.llytho.lootjs;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootContextData;
import com.github.llytho.lootjs.core.ILootModification;
import com.github.llytho.lootjs.core.LootModificationDebug;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class LootModificationsAPI {

    @Nullable
    public static BiConsumer<LootContext, LootModificationDebug> DEBUG_ACTION;

    private static LootModificationsAPI instance;
    private final List<ILootModification> modifications;

    public LootModificationsAPI() {
        modifications = new ArrayList<>();
    }

    public static LootModificationsAPI get() {
        if (instance == null) {
            instance = new LootModificationsAPI();
        }

        return instance;
    }

    public static void reload() {
        instance = null;
    }

    public void invokeActions(List<ItemStack> loot, LootContext context) {
        ILootContextData contextData = context.getParamOrNull(Constants.DATA);
        assert contextData != null;

        if (isFireBlock(context)) {
            return;
        }

        LootModificationDebug debug = context.getParamOrNull(Constants.RESULT_LOGGER);

        // TODO more testing here. I don't really know why there are empty items in the list or better:
        // TODO There are items which refer to the correct item but their cache flag is true so it acts like air
        loot.removeIf(ItemStack::isEmpty);
        contextData.setGeneratedLoot(loot);
        for (ILootModification modification : modifications) {
            if (modification.shouldExecute(context)) {
                if (debug != null) debug.write(modification.getName());
                modification.execute(context);
            }
            contextData.reset();
        }

        if (debug != null && DEBUG_ACTION != null) {
            DEBUG_ACTION.accept(context, debug);
        }
    }

    public void addModification(ILootModification modification) {
        modifications.add(modification);
    }

    private boolean isFireBlock(LootContext context) {
        BlockState b = context.getParamOrNull(LootParameters.BLOCK_STATE);
        return b != null && b.getBlock().is(Blocks.FIRE);
    }
}
