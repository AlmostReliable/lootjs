package com.github.llytho.lootjs.loot.action;

import com.github.llytho.lootjs.core.ILootAction;
import com.github.llytho.lootjs.util.LootContextUtils;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;

public class ConditionalAction implements ILootAction {

    private final ILootAction action;
    private final ILootCondition[] conditions;

    public ConditionalAction(ILootAction action, ILootCondition[] conditions) {
        this.action = action;
        this.conditions = conditions;
    }

    @Override
    public boolean accept(LootContext context) {
        for (ILootCondition condition : conditions) {
            boolean succeed = condition.test(context);
            LootContextUtils.writeConditionInLayer(context, succeed, condition);
            if (!succeed) {
                return false;
            }
        }

        boolean actionSucceed = action.accept(context);
        LootContextUtils.writeActionInLayer(context, actionSucceed, action);
        return actionSucceed;
    }
}
