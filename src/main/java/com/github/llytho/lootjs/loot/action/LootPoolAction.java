package com.github.llytho.lootjs.loot.action;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootAction;
import com.github.llytho.lootjs.core.ILootHandler;
import com.github.llytho.lootjs.loot.results.Icon;
import com.github.llytho.lootjs.loot.results.Info;
import com.github.llytho.lootjs.loot.results.LootInfoCollector;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LootPoolAction implements ILootAction {
    protected final NumberProvider numberProvider;
    protected final List<ILootHandler> handlers;

    public LootPoolAction(NumberProvider numberProvider, Collection<ILootHandler> handlers) {
        this.numberProvider = numberProvider;
        this.handlers = new ArrayList<>(handlers);
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        LootInfoCollector collector = context.getParamOrNull(Constants.RESULT_COLLECTOR);
        int rolls = numberProvider.getInt(context);
        for (int i = 1; i <= rolls; i++) {
            Info info = LootInfoCollector.createInfo(collector,
                    new Info.Composite(Icon.DICE, "Roll " + i + " out of " + rolls));
            List<ItemStack> poolLoot = new ArrayList<>();
            roll(context, poolLoot, collector);
            loot.addAll(poolLoot);
            LootInfoCollector.finalizeInfo(collector, info);
        }
        return true;
    }

    private void roll(LootContext context, List<ItemStack> loot, @Nullable LootInfoCollector collector) {
        for (ILootHandler handler : handlers) {
            Info info = LootInfoCollector.create(collector, handler);
            boolean result = handler.applyLootHandler(context, loot);
            LootInfoCollector.finalizeInfo(collector, info, result);
            if (!result) {
                break;
            }
        }
    }
}

