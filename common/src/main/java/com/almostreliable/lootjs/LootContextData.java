package com.almostreliable.lootjs;

import com.almostreliable.lootjs.core.ILootContextData;
import com.almostreliable.lootjs.core.LootContextType;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LootContextData implements ILootContextData {
    private final LootContextType type;
    private final Map<String, Object> customData;
    private boolean canceled;
    private List<ItemStack> generatedLoot;

    public LootContextData(LootContextType pType) {
        this.type = pType;
        this.customData = new HashMap<>();
        this.generatedLoot = new ArrayList<>();
    }

    @Override
    public LootContextType getLootContextType() {
        return type;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean flag) {
        canceled = flag;
    }

    @Override
    public Map<String, Object> getCustomData() {
        return customData;
    }

    @Override
    public List<ItemStack> getGeneratedLoot() {
        return generatedLoot;
    }

    @Override
    public void setGeneratedLoot(List<ItemStack> loot) {
        this.generatedLoot = loot;
    }

    @Override
    public void reset() {
        setCanceled(false);
    }
}
