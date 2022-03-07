package com.github.llytho.lootjs.core;

import com.github.llytho.lootjs.loot.results.Icon;
import com.github.llytho.lootjs.loot.results.Info;
import com.github.llytho.lootjs.loot.results.LootInfoCollector;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;

public abstract class AbstractLootModification implements ILootModification {
    protected final List<ILootHandler> handlers;
    private final String name;

    public AbstractLootModification(String name, List<ILootHandler> handlers) {
        this.name = name;
        this.handlers = handlers;
    }

    @Override
    public boolean execute(LootContext context) {
        LootInfoCollector collector = context.getParamOrNull(Constants.RESULT_COLLECTOR);
        Info info = LootInfoCollector.createInfo(collector, new Info.Composite(Icon.WRENCH, getName()));
        for (ILootHandler handler : handlers) {
            Info actionInfo = LootInfoCollector.create(collector, handler);
            boolean result = handler.test(context);
            LootInfoCollector.finalizeInfo(collector, actionInfo, result);
            if (!result) {
                LootInfoCollector.finalizeInfo(collector, info);
                return false;
            }
        }
        LootInfoCollector.finalizeInfo(collector, info);
        return true;
    }

    @Override
    public String getName() {
        return name;
    }
}
