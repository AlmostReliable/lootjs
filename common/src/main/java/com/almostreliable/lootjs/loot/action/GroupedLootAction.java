package com.almostreliable.lootjs.loot.action;

import com.almostreliable.lootjs.core.Constants;
import com.almostreliable.lootjs.core.ILootHandler;
import com.almostreliable.lootjs.loot.results.Icon;
import com.almostreliable.lootjs.loot.results.Info;
import com.almostreliable.lootjs.loot.results.LootInfoCollector;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GroupedLootAction extends CompositeLootAction {
    protected final NumberProvider numberProvider;

    public GroupedLootAction(NumberProvider numberProvider, Collection<ILootHandler> handlers) {
        super(handlers);
        this.numberProvider = numberProvider;
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        LootInfoCollector collector = context.getParamOrNull(Constants.RESULT_COLLECTOR);
        int rolls = numberProvider.getInt(context);
        for (int i = 1; i <= rolls; i++) {
            Info info = createInfoIfMultipleRolls(rolls, i, collector);
            List<ItemStack> poolLoot = new ArrayList<>();
            run(context, poolLoot, collector);
            loot.addAll(poolLoot);
            LootInfoCollector.finalizeInfo(collector, info);
        }
        return true;
    }

    @Nullable
    private Info createInfoIfMultipleRolls(int rolls, int currentRoll, @Nullable LootInfoCollector collector) {
        if (rolls == 1) {
            return null;
        }

        return LootInfoCollector.createInfo(
                collector,
                new Info.Composite(Icon.DICE, "Roll " + currentRoll + " out of " + rolls)
        );
    }
}

