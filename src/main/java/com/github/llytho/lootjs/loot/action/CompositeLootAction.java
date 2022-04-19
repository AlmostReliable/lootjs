package com.github.llytho.lootjs.loot.action;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootAction;
import com.github.llytho.lootjs.core.ILootHandler;
import com.github.llytho.lootjs.loot.results.Info;
import com.github.llytho.lootjs.loot.results.LootInfoCollector;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public class CompositeLootAction implements ILootAction {
    protected final Collection<ILootHandler> handlers;

    public CompositeLootAction(Collection<ILootHandler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        LootInfoCollector collector = context.getParamOrNull(Constants.RESULT_COLLECTOR);
        return run(context, loot, collector);
    }

    protected boolean run(LootContext context, List<ItemStack> loot, @Nullable LootInfoCollector collector) {
        for (ILootHandler handler : handlers) {
            Info info = LootInfoCollector.create(collector, handler);
            boolean result = handler.applyLootHandler(context, loot);
            LootInfoCollector.finalizeInfo(collector, info, result);
            if (!result) {
                return false;
            }
        }

        return true;
    }
}
