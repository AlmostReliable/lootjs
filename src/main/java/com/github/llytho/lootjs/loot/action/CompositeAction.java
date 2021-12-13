package com.github.llytho.lootjs.loot.action;

import com.github.llytho.lootjs.core.IAction;
import net.minecraft.loot.LootContext;


public class CompositeAction implements IAction<LootContext> {

    private final IAction<LootContext>[] actions;

    public CompositeAction(IAction<LootContext>[] actions) {
        this.actions = actions;
    }

    @Override
    public boolean accept(LootContext context) {
        for (IAction<LootContext> action : actions) {
            if (!action.accept(context)) {
                return false;
            }
        }

        return true;
    }

}
