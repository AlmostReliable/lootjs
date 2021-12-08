package com.github.llytho.lootjs.action;

import net.minecraft.loot.LootContext;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ConditionalLootAction implements Consumer<LootContext> {

    private final Consumer<LootContext> pAction;
    private final Predicate<LootContext> pPredicate;

    public ConditionalLootAction(Consumer<LootContext> pAction, Predicate<LootContext> pPredicate) {
        this.pAction = pAction;
        this.pPredicate = pPredicate;
    }

    @Override
    public void accept(LootContext pContext) {
        if (pPredicate.test(pContext)) {
            pAction.accept(pContext);
        }
    }
}
