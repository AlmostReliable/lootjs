package com.github.llytho.lootjs.loot.condition;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootHandler;
import com.github.llytho.lootjs.loot.results.Info;
import com.github.llytho.lootjs.loot.results.LootInfoCollector;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class NotCondition implements IExtendedLootCondition {
    private final LootItemCondition condition;

    public NotCondition(LootItemCondition condition) {
        this.condition = condition;
    }

    @Override
    public boolean test(LootContext context) {
        LootInfoCollector collector = context.getParamOrNull(Constants.RESULT_COLLECTOR);
        Info info = LootInfoCollector.create(collector, (ILootHandler) condition);
        boolean result = !condition.test(context);
        LootInfoCollector.finalizeInfo(collector, info, result);
        return result;
    }
}
