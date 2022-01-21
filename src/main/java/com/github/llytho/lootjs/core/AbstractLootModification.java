package com.github.llytho.lootjs.core;

import net.minecraft.loot.LootContext;

import java.util.List;

public abstract class AbstractLootModification implements ILootModification {
    public static final String UNUSED_NAME = "NO_NAME_PROVIDED";
    protected final List<ILootAction> actions;
    private final String name;

    public AbstractLootModification(String name, List<ILootAction> actions) {
        this.name = name;
        this.actions = actions;
    }

    @Override
    public boolean execute(LootContext context) {
        DebugStack stack = context.getParamOrNull(Constants.RESULT_LOGGER);
        DebugStack.pushLayer(stack);
        for (ILootAction action : actions) {
            if (!action.accept(context)) {
                DebugStack.popLayer(stack);
                return false;
            }
        }

        DebugStack.popLayer(stack);
        return true;
    }

    @Override
    public String getName() {
        return name;
    }
}
