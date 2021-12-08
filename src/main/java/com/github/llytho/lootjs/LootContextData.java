package com.github.llytho.lootjs;

import com.github.llytho.lootjs.core.ILootContextData;
import com.github.llytho.lootjs.core.LootContextType;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LootContextData implements ILootContextData {
    private final LootContextType type;
    private final Map<String, Object> additionalData;
    private boolean canceled;
    private List<ItemStack> generatedLoot;

    public LootContextData(LootContextType pType) {
        this.type = pType;
        this.additionalData = new HashMap<>();
        this.generatedLoot = new ArrayList<>();
    }

    public LootContextType getLootContextType() {
        return type;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean pFlag) {
        canceled = pFlag;
    }

    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }

    public List<ItemStack> getGeneratedLoot() {
        return generatedLoot;
    }

    public void setGeneratedLoot(List<ItemStack> pLoot) {
        this.generatedLoot = pLoot;
    }

    public void reset() {
        setCanceled(false);
    }
}
