package com.github.llytho.lootjs.core;

import net.minecraft.loot.LootContext;

import java.util.List;

public abstract class AbstractLootModification implements ILootModification {
    protected final List<LootAction> actions;

    public AbstractLootModification(List<LootAction> actions) {
        this.actions = actions;
    }

    @Override
    public boolean execute(LootContext context) {
        for (LootAction action : actions) {
            if (!action.accept(context)) {
                return false;
            }
        }

        return true;
    }
}
