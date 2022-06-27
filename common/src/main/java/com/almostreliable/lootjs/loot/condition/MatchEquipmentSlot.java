package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.util.LootContextUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.function.Predicate;

public class MatchEquipmentSlot implements IExtendedLootCondition {
    private final Predicate<ItemStack> predicate;
    private final EquipmentSlot slot;

    public MatchEquipmentSlot(EquipmentSlot slot, Predicate<ItemStack> predicate) {
        this.slot = slot;
        this.predicate = predicate;
    }

    @Override
    public boolean test(LootContext context) {
        ServerPlayer player = LootContextUtils.getPlayerOrNull(context);
        return player != null && predicate.test(player.getItemBySlot(slot));
    }
}
