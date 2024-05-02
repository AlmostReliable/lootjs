package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.util.LootContextUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.storage.loot.LootContext;

public class MatchEquipmentSlot implements IExtendedLootCondition {
    private final ItemFilter itemFilter;
    private final EquipmentSlot slot;

    public MatchEquipmentSlot(EquipmentSlot slot, ItemFilter itemFilter) {
        this.slot = slot;
        this.itemFilter = itemFilter;
    }

    @Override
    public boolean test(LootContext context) {
        ServerPlayer player = LootContextUtils.getPlayerOrNull(context);
        return player != null && itemFilter.test(player.getItemBySlot(slot));
    }
}
