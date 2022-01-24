package com.github.llytho.lootjs.core;

import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;

public interface ILootContextData {

    LootContextType getLootContextType();

    boolean isCanceled();

    void setCanceled(boolean flag);

    Map<String, Object> getCustomData();

    List<ItemStack> getGeneratedLoot();

    void setGeneratedLoot(List<ItemStack> loot);

    void reset();
}
