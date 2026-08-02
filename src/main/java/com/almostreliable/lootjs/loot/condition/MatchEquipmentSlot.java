package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.LootJSConditions;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.util.LootContextUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record MatchEquipmentSlot(EquipmentSlot slot, ItemFilter itemFilter) implements LootItemCondition {

    @Override
    public boolean test(LootContext context) {
        ServerPlayer player = LootContextUtils.getPlayerOrNull(context);
        return player != null && itemFilter.test(player.getItemBySlot(slot));
    }

    @Override
    public MapCodec<? extends LootItemCondition> codec() {
        return LootJSConditions.MATCH_EQUIP.value();
    }
}
