package com.github.llytho.lootjs.kube.action;

import com.github.llytho.lootjs.kube.context.LootContextJS;
import net.minecraft.loot.LootContext;

import java.util.function.Consumer;

public class CustomJSAction implements Consumer<LootContext> {

    private final Consumer<LootContextJS> action;

    public CustomJSAction(Consumer<LootContextJS> pAction) {
        action = pAction;
    }

    @Override
    public void accept(LootContext pContext) {
        LootContextJS contextJS = new LootContextJS(pContext);
        action.accept(contextJS);
    }
}
