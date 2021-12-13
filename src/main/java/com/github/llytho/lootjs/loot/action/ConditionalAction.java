package com.github.llytho.lootjs.loot.action;

import com.github.llytho.lootjs.core.IAction;
import net.minecraft.loot.LootContext;

import java.util.function.Predicate;

public class ConditionalAction implements IAction<LootContext> {

    private final IAction<LootContext> actions;
    private final Predicate<LootContext> predicate;

    public ConditionalAction(IAction<LootContext> actions, Predicate<LootContext> predicate) {
        this.actions = actions;
        this.predicate = predicate;
    }

    @Override
    public boolean accept(LootContext context) {
        return predicate.test(context) && actions.accept(context);
    }
}
