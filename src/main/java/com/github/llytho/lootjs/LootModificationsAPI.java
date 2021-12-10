package com.github.llytho.lootjs;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.IAction;
import com.github.llytho.lootjs.core.ILootContextData;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;

import java.util.ArrayList;
import java.util.List;

public class LootModificationsAPI {

    private static LootModificationsAPI instance;
    private final List<IAction<LootContext>> actions;

    public LootModificationsAPI() {
        actions = new ArrayList<>();
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

    public void invokeActions(List<ItemStack> pLoot, LootContext pContext) {
        ILootContextData contextData = pContext.getParamOrNull(Constants.DATA);
        assert contextData != null;

        // TODO more testing here. I don't really know why there are empty items in the list or better:
        // TODO There are items which refer to the correct item but their cache flag is true so it acts like air
        pLoot.removeIf(ItemStack::isEmpty);
        contextData.setGeneratedLoot(pLoot);
        for (IAction<LootContext> action : actions) {
            action.accept(pContext);
            contextData.reset();
        }
    }

    public void addAction(IAction<LootContext> pAction) {
        actions.add(pAction);
    }
}
