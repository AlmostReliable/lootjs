package com.github.llytho.lootjs.loot.condition;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.DebugStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class OrCondition implements IExtendedLootCondition {
    private final LootItemCondition[] conditions;

    public OrCondition(LootItemCondition[] conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean test(LootContext context) {
        DebugStack stack = context.getParamOrNull(Constants.RESULT_LOGGER);
        DebugStack.TestedEntry entry = DebugStack.write(stack, null, this, " {", false);
        DebugStack.pushLayer(stack);

        boolean succeed = false;
        for (LootItemCondition condition : conditions) {
            succeed = condition.test(context);
            if (!(condition instanceof OrCondition || condition instanceof AndCondition)) {
                DebugStack.write(stack, DebugStack.CONDITION_PREFIX, condition, null, succeed);
            }
            if (succeed) {
                break;
            }
        }

        DebugStack.popLayer(stack);
        DebugStack.write(stack, "}");
        if (entry != null) entry.setSucceed(succeed);
        return succeed;
    }
}
