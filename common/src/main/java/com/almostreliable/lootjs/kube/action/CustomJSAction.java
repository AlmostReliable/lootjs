package com.almostreliable.lootjs.kube.action;

import com.almostreliable.lootjs.core.ILootAction;
import com.almostreliable.lootjs.kube.LootContextJS;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;
import java.util.function.Consumer;

public class CustomJSAction implements ILootAction {

    private final Consumer<LootContextJS> action;

    public CustomJSAction(Consumer<LootContextJS> pAction) {
        action = pAction;
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        LootContextJS contextJS = new LootContextJS(context);
        action.accept(contextJS);
        return !contextJS.isCanceled();
    }
}
