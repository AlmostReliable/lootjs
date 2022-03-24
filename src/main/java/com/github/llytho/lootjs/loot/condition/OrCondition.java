package com.github.llytho.lootjs.loot.condition;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootCondition;
import com.github.llytho.lootjs.core.ILootContextData;
import com.github.llytho.lootjs.kube.ConditionsContainer;
import com.github.llytho.lootjs.loot.results.Info;
import com.github.llytho.lootjs.loot.results.LootInfoCollector;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.ArrayList;
import java.util.List;

public class OrCondition implements IExtendedLootCondition {
    private final ILootCondition[] conditions;

    public OrCondition(ILootCondition[] conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        LootInfoCollector collector = context.getParamOrNull(Constants.RESULT_COLLECTOR);
        for (ILootCondition condition : conditions) {
            Info info = LootInfoCollector.create(collector, condition);
            boolean result = condition.applyLootHandler(context, loot);
            LootInfoCollector.finalizeInfo(collector, info, result);
            if (result) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean test(LootContext context) {
        ILootContextData data = context.getParamOrNull(Constants.DATA);
        return applyLootHandler(context, data == null ? new ArrayList<>() : data.getGeneratedLoot());
    }

    public static class Builder implements ConditionsContainer<Builder> {

        private final List<ILootCondition> conditions = new ArrayList<>();

        public OrCondition build() {
            if (conditions.isEmpty()) {
                throw new IllegalArgumentException("No conditions set for `or`");
            }

            return new OrCondition(conditions.toArray(new ILootCondition[0]));
        }

        @Override
        public Builder addCondition(ILootCondition condition) {
            conditions.add(condition);
            return this;
        }
    }
}
