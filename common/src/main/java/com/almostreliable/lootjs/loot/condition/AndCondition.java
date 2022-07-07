package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.core.LootJSParamSets;
import com.almostreliable.lootjs.core.ILootCondition;
import com.almostreliable.lootjs.core.ILootContextData;
import com.almostreliable.lootjs.loot.results.Info;
import com.almostreliable.lootjs.loot.results.LootInfoCollector;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.ArrayList;
import java.util.List;

public class AndCondition implements IExtendedLootCondition {
    private final ILootCondition[] conditions;

    public AndCondition(ILootCondition... conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean test(LootContext context) {
        ILootContextData data = context.getParamOrNull(LootJSParamSets.DATA);
        return applyLootHandler(context, data == null ? new ArrayList<>() : data.getGeneratedLoot());
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        LootInfoCollector collector = context.getParamOrNull(LootJSParamSets.RESULT_COLLECTOR);
        for (ILootCondition condition : conditions) {
            Info info = LootInfoCollector.create(collector, condition);
            boolean result = condition.applyLootHandler(context, loot);
            LootInfoCollector.finalizeInfo(collector, info, result);
            if (!result) {
                return false;
            }
        }
        return true;
    }
}
