package com.github.llytho.lootjs;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootContextData;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LootModificationsAPI {

    private static LootModificationsAPI instance;
    private final List<Consumer<LootContext>> actions;

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

        contextData.setGeneratedLoot(pLoot);
        for (Consumer<LootContext> action : actions) {
            action.accept(pContext);
            contextData.reset();
        }
    }

    public void addAction(Consumer<LootContext> pAction) {
        actions.add(pAction);
    }
}
