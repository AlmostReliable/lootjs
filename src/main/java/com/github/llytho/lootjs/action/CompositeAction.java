package com.github.llytho.lootjs.action;

import com.github.llytho.lootjs.core.IAction;
import net.minecraft.loot.LootContext;


public class CompositeAction implements IAction<LootContext> {

    private final IAction<LootContext>[] actions;

    public CompositeAction(IAction<LootContext>[] pActions) {
        this.actions = pActions;
    }

    @Override
    public boolean accept(LootContext pContext) {
        for (IAction<LootContext> action : actions) {
            if (!action.accept(pContext)) {
                return false;
            }
        }

        return true;
    }

}
