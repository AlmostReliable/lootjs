package com.github.llytho.lootjs.action;

import com.github.llytho.lootjs.core.IAction;
import net.minecraft.loot.LootContext;

import java.util.function.Predicate;

public class ConditionalAction implements IAction<LootContext> {

    private final IAction<LootContext> pAction;
    private final Predicate<LootContext> pPredicate;

    public ConditionalAction(IAction<LootContext> pAction, Predicate<LootContext> pPredicate) {
        this.pAction = pAction;
        this.pPredicate = pPredicate;
    }

    @Override
    public boolean accept(LootContext pContext) {
        return pPredicate.test(pContext) && pAction.accept(pContext);
    }
}
