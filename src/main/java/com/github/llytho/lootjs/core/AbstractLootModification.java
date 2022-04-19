package com.github.llytho.lootjs.core;

import com.github.llytho.lootjs.loot.action.CompositeLootAction;
import com.github.llytho.lootjs.loot.results.Icon;
import com.github.llytho.lootjs.loot.results.Info;
import com.github.llytho.lootjs.loot.results.LootInfoCollector;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Collection;
import java.util.List;

public abstract class AbstractLootModification extends CompositeLootAction {
    private final String name;

    public AbstractLootModification(String name, Collection<ILootHandler> handlers) {
        super(handlers);
        this.name = name;
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        if (shouldExecute(context)) {
            LootInfoCollector collector = context.getParamOrNull(Constants.RESULT_COLLECTOR);
            Info info = LootInfoCollector.createInfo(collector, new Info.Composite(Icon.WRENCH, name));
            run(context, loot, collector);
            LootInfoCollector.finalizeInfo(collector, info);
        }

        return true;
    }

    public abstract boolean shouldExecute(LootContext context);
}
