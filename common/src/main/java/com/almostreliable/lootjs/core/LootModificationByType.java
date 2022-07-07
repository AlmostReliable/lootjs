package com.almostreliable.lootjs.core;

import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;

public class LootModificationByType extends AbstractLootModification {
    private final List<LootContextType> types;

    public LootModificationByType(String name, List<LootContextType> types, List<ILootHandler> handlers) {
        super(name, handlers);
        this.types = types;
    }

    @Override
    public boolean shouldExecute(LootContext context) {
        ILootContextData data = context.getParamOrNull(LootJSParamSets.DATA);
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
