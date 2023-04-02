package com.almostreliable.lootjs.forge.filters;

import com.almostreliable.lootjs.filters.ItemFilter;
import net.minecraftforge.common.ToolAction;

public interface ForgeItemFilter extends ItemFilter {
    static ItemFilter canPerformAnyAction(String... actions) {
        ToolAction[] toolActions = getToolActions(actions);
        return itemStack -> {
            for (ToolAction action : toolActions) {
                if (itemStack.canPerformAction(action)) {
                    return true;
                }
            }
            return false;
        };
    }

    static ItemFilter canPerformAction(String... actions) {
        ToolAction[] toolActions = getToolActions(actions);
        return itemStack -> {
            for (ToolAction action : toolActions) {
                if (!itemStack.canPerformAction(action)) {
                    return false;
                }
            }
            return true;
        };
    }

    private static ToolAction[] getToolActions(String[] actions) {
        ToolAction[] toolActions = new ToolAction[actions.length];
        for (int i = 0; i < actions.length; i++) {
            toolActions[i] = ToolAction.get(actions[i]);
        }
        return toolActions;
    }
}
