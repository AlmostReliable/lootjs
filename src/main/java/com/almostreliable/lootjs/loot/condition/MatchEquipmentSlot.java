package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.LootJSConditions;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.util.LootContextUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class MatchEquipmentSlot implements LootItemCondition {
    private final ItemFilter itemFilter;
    private final EquipmentSlot slot;

    public MatchEquipmentSlot() {
        this(EquipmentSlot.MAINHAND, ItemFilter.NONE);
    }

    public MatchEquipmentSlot(EquipmentSlot slot, ItemFilter itemFilter) {
        this.slot = slot;
        this.itemFilter = itemFilter;
    }

    @Override
    public boolean test(LootContext context) {
        ServerPlayer player = LootContextUtils.getPlayerOrNull(context);
        return player != null && itemFilter.test(player.getItemBySlot(slot));
    }

    @Override
    public LootItemConditionType getType() {
        return LootJSConditions.MATCH_EQUIP.value();
    }
}
