package com.github.llytho.lootjs.condition;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootContextData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class MatchEquipmentSlot implements IExtendedLootCondition {

    private final Predicate<ItemStack> predicate;
    private final EquipmentSlotType slot;

    public MatchEquipmentSlot(EquipmentSlotType pSlot, Predicate<ItemStack> pPredicate) {
        this.slot = pSlot;
        this.predicate = pPredicate;
    }

    @Override
    public boolean test(LootContext pContext) {
        ILootContextData data = pContext.getParamOrNull(Constants.DATA);
        if (data == null) return false;

        switch (data.getLootContextType()) {
            case BLOCK:
            case CHEST:
                return matchSlot(pContext.getParamOrNull(LootParameters.THIS_ENTITY));
            case ENTITY:
            case FISHING:
                return matchSlot(pContext.getParamOrNull(LootParameters.KILLER_ENTITY));
        }

        return false;
    }

    private boolean matchSlot(@Nullable Entity pEntity) {
        if (pEntity instanceof ServerPlayerEntity) {
            return predicate.test(((ServerPlayerEntity) pEntity).getItemBySlot(slot));
        }

        return false;
    }
}
