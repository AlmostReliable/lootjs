package com.github.llytho.lootjs.loot.action;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.DebugStack;
import com.github.llytho.lootjs.core.ILootAction;
import com.github.llytho.lootjs.loot.condition.AndCondition;
import com.github.llytho.lootjs.loot.condition.OrCondition;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;

public class ConditionalAction implements ILootAction {

    public static final String ACTION_PREFIX_INVOKED = "\u27A5 invoke ";
    public static final String ACTION_PREFIX_NOT_INVOKED = "\u27A5 conditions are false. Stopping at ";

    private final ILootAction action;
    private final ILootCondition[] conditions;

    public ConditionalAction(ILootAction action, ILootCondition[] conditions) {
        this.action = action;
        this.conditions = conditions;
    }

    @Override
    public boolean accept(LootContext context) {
        DebugStack stack = context.getParamOrNull(Constants.RESULT_LOGGER);
        for (ILootCondition condition : conditions) {
            boolean succeed = condition.test(context);
            if (!(condition instanceof OrCondition || condition instanceof AndCondition)) {
                DebugStack.write(stack, DebugStack.CONDITION_PREFIX, condition, null, succeed);
            }
            if (!succeed) {
                DebugStack.write(stack, ACTION_PREFIX_NOT_INVOKED, action);
                return false;
            }
        }

        boolean actionSucceed = action.accept(context);
        DebugStack.write(stack, ACTION_PREFIX_INVOKED, action);
        return actionSucceed;
    }
}
