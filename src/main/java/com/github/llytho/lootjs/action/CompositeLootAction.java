package com.github.llytho.lootjs.action;

import net.minecraft.loot.LootContext;

import java.util.function.Consumer;


public class CompositeLootAction implements Consumer<LootContext> {

    private final Consumer<LootContext>[] actions;

    public CompositeLootAction(Consumer<LootContext>[] pActions) {
        this.actions = pActions;
    }

    @Override
    public void accept(LootContext pContext) {
        for (Consumer<LootContext> action : actions) {
            action.accept(pContext);
        }
    }

}
