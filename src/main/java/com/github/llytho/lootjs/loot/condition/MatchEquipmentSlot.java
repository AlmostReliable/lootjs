package com.github.llytho.lootjs.loot.condition;

import com.github.llytho.lootjs.util.LootContextUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;

import java.util.function.Predicate;

public class MatchEquipmentSlot implements IExtendedLootCondition {
    private final Predicate<ItemStack> predicate;
    private final EquipmentSlotType slot;

    public MatchEquipmentSlot(EquipmentSlotType slot, Predicate<ItemStack> predicate) {
        this.slot = slot;
        this.predicate = predicate;
    }

    @Override
    public boolean test(LootContext context) {
        ServerPlayerEntity player = LootContextUtils.getPlayerOrNull(context);
        return player != null && predicate.test(player.getItemBySlot(slot));
    }
}
