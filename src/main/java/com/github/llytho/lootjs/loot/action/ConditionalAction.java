package com.github.llytho.lootjs.loot.action;

import com.github.llytho.lootjs.core.LootAction;
import com.github.llytho.lootjs.util.LootContextUtils;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;

public class ConditionalAction implements LootAction {

    private final LootAction action;
    private final ILootCondition[] predicates;

    public ConditionalAction(LootAction action, ILootCondition[] predicates) {
        this.action = action;
        this.predicates = predicates;
    }

    @Override
    public boolean accept(LootContext context) {
        LootContextUtils.pushResultLayer(context);
        for (ILootCondition predicate : predicates) {
            boolean succeed = predicate.test(context);
            LootContextUtils.writeResult(context, succeed, predicate);
            if (!succeed) {
                return false;
            }
        }

        return action.accept(context);
    }
}
