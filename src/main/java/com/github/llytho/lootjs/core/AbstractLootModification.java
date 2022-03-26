package com.github.llytho.lootjs.core;

import com.github.llytho.lootjs.loot.action.CompositeLootAction;
import com.github.llytho.lootjs.loot.results.Icon;
import com.github.llytho.lootjs.loot.results.Info;
import com.github.llytho.lootjs.loot.results.LootInfoCollector;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;

public abstract class AbstractLootModification implements ILootModification {
    protected final List<ILootHandler> handlers;
    private final String name;
    private final CompositeLootAction composite;

    public AbstractLootModification(String name, List<ILootHandler> handlers) {
        this.name = name;
        this.handlers = handlers;
        this.composite = new CompositeLootAction(handlers);
    }

    @Override
    public boolean execute(LootContext context, List<ItemStack> loot) {
        LootInfoCollector collector = context.getParamOrNull(Constants.RESULT_COLLECTOR);
        Info info = LootInfoCollector.createInfo(collector, new Info.Composite(Icon.WRENCH, getName()));
        boolean result = composite.applyLootHandler(context, loot);
        LootInfoCollector.finalizeInfo(collector, info);
        return result;
    }

    public abstract boolean shouldExecute(LootContext context);

    @Override
    public String getName() {
        return name;
    }
}
