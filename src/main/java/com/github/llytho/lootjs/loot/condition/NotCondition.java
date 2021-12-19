package com.github.llytho.lootjs.loot.condition;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.DebugStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;

public class NotCondition implements IExtendedLootCondition {
    public static final String PREFIX = DebugStack.CONDITION_PREFIX + "NOT_";
    private final ILootCondition condition;

    public NotCondition(ILootCondition condition) {
        this.condition = condition;
    }

    @Override
    public boolean test(LootContext context) {
        DebugStack stack = context.getParamOrNull(Constants.RESULT_LOGGER);
        boolean succeed = !condition.test(context);
        DebugStack.write(stack, PREFIX, condition, null, succeed);
        return succeed;
    }
}
