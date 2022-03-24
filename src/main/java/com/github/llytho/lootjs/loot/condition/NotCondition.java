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

public class NotCondition implements IExtendedLootCondition {
    private final ILootCondition condition;

    public NotCondition(ILootCondition condition) {
        this.condition = condition;
    }

    @Override
    public boolean test(LootContext context) {
        ILootContextData data = context.getParamOrNull(Constants.DATA);
        return applyLootHandler(context, data == null ? new ArrayList<>() : data.getGeneratedLoot());
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        LootInfoCollector collector = context.getParamOrNull(Constants.RESULT_COLLECTOR);
        Info info = LootInfoCollector.create(collector, condition);
        boolean result = !condition.applyLootHandler(context, loot);
        LootInfoCollector.finalizeInfo(collector, info, result);
        return result;
    }

    public static class Builder implements ConditionsContainer<Builder> {

        private ILootCondition condition = null;

        public NotCondition build() {
            if (condition == null) {
                throw new IllegalArgumentException("No condition was set");
            }

            return new NotCondition(condition);
        }

        @Override
        public Builder addCondition(ILootCondition condition) {
            if (this.condition != null) {
                throw new IllegalArgumentException("Already set a condition");
            }

            this.condition = condition;
            return this;
        }
    }
}
