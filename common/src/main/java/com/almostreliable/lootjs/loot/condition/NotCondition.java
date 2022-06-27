package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.core.Constants;
import com.almostreliable.lootjs.core.ILootCondition;
import com.almostreliable.lootjs.core.ILootContextData;
import com.almostreliable.lootjs.loot.results.Info;
import com.almostreliable.lootjs.loot.results.LootInfoCollector;
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
}
