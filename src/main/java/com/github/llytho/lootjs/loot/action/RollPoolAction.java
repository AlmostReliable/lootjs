package com.github.llytho.lootjs.loot.action;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.DebugStack;
import com.github.llytho.lootjs.core.ILootAction;
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
    protected final List<ILootAction> actions;

    public RollPoolAction(MinMaxBounds.Ints interval, Collection<ILootAction> actions) {
        this.interval = interval;
        this.actions = new ArrayList<>(actions);
    }

    @Override
    public boolean accept(LootContext context) {
        // TODO just copied from AbstractLootModification. Refactor this later.
        DebugStack stack = context.getParamOrNull(Constants.RESULT_LOGGER);
        int rolls = getRolls(context.getRandom());
        DebugStack.pushLayer(stack);
        DebugStack.write(stack, "Rolling " + rolls + " times {");
        for (int i = 1; i <= rolls; i++) {
            rollPool(context, stack, i);
        }
        DebugStack.write(stack, "}");
        DebugStack.popLayer(stack);
        return true;
    }

    private void rollPool(LootContext context, @Nullable DebugStack stack, int rollNumber) {
        DebugStack.pushLayer(stack);
        DebugStack.write(stack, "Roll " + rollNumber + " {");
        DebugStack.pushLayer(stack);
        for (ILootAction action : actions) {
            if (!action.accept(context)) {
                break;
            }
        }
        DebugStack.popLayer(stack);
        DebugStack.write(stack, "}");
        DebugStack.popLayer(stack);
    }


    protected int getRolls(Random random) {
        int min = ObjectUtils.firstNonNull(interval.getMin(), 1);
        int max = ObjectUtils.firstNonNull(interval.getMax(), min);
        return random.nextInt(max + 1 - min) + min;
    }
}
