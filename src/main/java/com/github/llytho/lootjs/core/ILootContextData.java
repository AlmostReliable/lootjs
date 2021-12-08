package com.github.llytho.lootjs.core;

import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Map;

public interface ILootContextData {
    LootContextType getLootContextType();

    boolean isCanceled();

    void setCanceled(boolean pFlag);

    Map<String, Object> getAdditionalData();

    List<ItemStack> getGeneratedLoot();

    void setGeneratedLoot(List<ItemStack> pLoot);

    void reset();
}
