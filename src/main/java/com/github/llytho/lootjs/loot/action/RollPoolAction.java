package com.github.llytho.lootjs.loot.action;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootAction;
import com.github.llytho.lootjs.core.ILootHandler;
import com.github.llytho.lootjs.loot.results.Icon;
import com.github.llytho.lootjs.loot.results.Info;
import com.github.llytho.lootjs.loot.results.LootInfoCollector;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.world.level.storage.loot.LootContext;
import org.apache.commons.lang3.ObjectUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class RollPoolAction implements ILootAction {
    protected final MinMaxBounds.Ints interval;
    protected final List<ILootHandler> handlers;

    public RollPoolAction(MinMaxBounds.Ints interval, Collection<ILootHandler> handlers) {
        this.interval = interval;
        this.handlers = new ArrayList<>(handlers);
    }

    @Override
    public boolean test(LootContext context) {
        LootInfoCollector collector = context.getParamOrNull(Constants.RESULT_COLLECTOR);
        int rolls = getRolls(context.getRandom());
        for (int i = 1; i <= rolls; i++) {
            Info info = LootInfoCollector.createInfo(collector,
                    new Info.Composite(Icon.DICE, "Roll " + i + " out of " + rolls));
            roll(context, collector);
            LootInfoCollector.finalizeInfo(collector, info);
        }
        return true;
    }

    private void roll(LootContext context, @Nullable LootInfoCollector collector) {
        for (ILootHandler handler : handlers) {
            Info info = LootInfoCollector.create(collector, handler);
            boolean result = handler.test(context);
            LootInfoCollector.finalizeInfo(collector, info, result);
            if (!result) {
                break;
            }
        }
    }


    protected int getRolls(Random random) {
        int min = ObjectUtils.firstNonNull(interval.getMin(), 1);
        int max = ObjectUtils.firstNonNull(interval.getMax(), min);
        return random.nextInt(max + 1 - min) + min;
    }
}
