package com.github.llytho.lootjs.condition;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootContextData;
import com.github.llytho.lootjs.core.LootContextType;
import net.minecraft.loot.LootContext;

public class IsLootTableType implements IExtendedLootCondition {

    private final LootContextType[] types;

    public IsLootTableType(LootContextType[] pTypes) {
        this.types = pTypes;
    }

    @Override
    public boolean test(LootContext pContext) {
        ILootContextData data = pContext.getParamOrNull(Constants.DATA);
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
