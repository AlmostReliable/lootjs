package com.github.llytho.lootjs.kube.action;

import com.github.llytho.lootjs.core.ILootAction;
import com.github.llytho.lootjs.kube.LootContextJS;
import net.minecraft.loot.LootContext;

import java.util.function.Consumer;

public class CustomJSAction implements ILootAction {

    private final Consumer<LootContextJS> action;

    public CustomJSAction(Consumer<LootContextJS> pAction) {
        action = pAction;
    }

    @Override
    public boolean accept(LootContext context) {
        LootContextJS contextJS = new LootContextJS(context);
        action.accept(contextJS);
        return !contextJS.isCanceled();
    }
}
