package com.github.llytho.lootjs.loot.condition;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootHandler;
import com.github.llytho.lootjs.loot.results.Info;
import com.github.llytho.lootjs.loot.results.LootInfoCollector;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class OrCondition implements IExtendedLootCondition {
    private final LootItemCondition[] conditions;

    public OrCondition(LootItemCondition[] conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean test(LootContext context) {
        LootInfoCollector collector = context.getParamOrNull(Constants.RESULT_COLLECTOR);
        for (LootItemCondition condition : conditions) {
            Info info = LootInfoCollector.create(collector, (ILootHandler) condition);
            boolean result = condition.test(context);
            LootInfoCollector.finalizeInfo(collector, info, result);
            if (result) {
                return true;
            }
        }
        return false;
    }
}
