package com.github.llytho.lootjs.predicate;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class MatchEquipmentPredicate implements Predicate<PlayerEntity> {
    private final Predicate<ItemStack> predicate;
    private final EquipmentSlotType slot;

    public MatchEquipmentPredicate(EquipmentSlotType slot, Predicate<ItemStack> predicate) {
        this.slot = slot;
        this.predicate = predicate;
    }

    @Override
    public boolean test(PlayerEntity playerEntity) {
        return predicate.test(playerEntity.getItemBySlot(slot));
    }
}
