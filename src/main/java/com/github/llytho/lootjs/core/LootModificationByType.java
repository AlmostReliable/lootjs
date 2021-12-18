package com.github.llytho.lootjs.core;

import net.minecraft.loot.LootContext;

import java.util.List;

public class LootModificationByType extends AbstractLootModification {

    private final String name;
    private final List<LootContextType> types;

    public LootModificationByType(String name, List<LootContextType> types, List<LootAction> actions) {
        super(actions);
        this.name = name;
        this.types = types;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean shouldExecute(LootContext context) {
        ILootContextData data = context.getParamOrNull(Constants.DATA);
        return data != null && matchesType(data);
    }

    private boolean matchesType(ILootContextData pData) {
        for (LootContextType type : types) {
            if (pData.getLootContextType() == type) {
                return true;
            }
        }
        return false;
    }
}
