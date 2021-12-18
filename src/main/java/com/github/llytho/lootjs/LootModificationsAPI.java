package com.github.llytho.lootjs;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootContextData;
import com.github.llytho.lootjs.core.ILootModification;
import com.github.llytho.lootjs.core.ILootModificationResult;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;

import java.util.ArrayList;
import java.util.List;

public class LootModificationsAPI {

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

        // TODO more testing here. I don't really know why there are empty items in the list or better:
        // TODO There are items which refer to the correct item but their cache flag is true so it acts like air
        loot.removeIf(ItemStack::isEmpty);
        contextData.setGeneratedLoot(loot);
        for (ILootModification modification : modifications) {
            if (modification.shouldExecute(context)) {
                modification.execute(context);
            }
            contextData.reset();
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
