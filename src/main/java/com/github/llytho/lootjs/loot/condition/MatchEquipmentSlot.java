package com.github.llytho.lootjs.loot.condition;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootContextData;
import com.github.llytho.lootjs.predicate.MatchEquipmentPredicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class MatchEquipmentSlot implements IExtendedLootCondition {
    MatchEquipmentPredicate predicate;

    public MatchEquipmentSlot(EquipmentSlotType slot, Predicate<ItemStack> predicate) {
        this.predicate = new MatchEquipmentPredicate(slot, predicate);
    }

    @Override
    public boolean test(LootContext context) {
        ILootContextData data = context.getParamOrNull(Constants.DATA);
        if (data == null) return false;

        switch (data.getLootContextType()) {
            case BLOCK:
            case CHEST:
                return matchSlot(context.getParamOrNull(LootParameters.THIS_ENTITY));
            case ENTITY:
            case FISHING:
                return matchSlot(context.getParamOrNull(LootParameters.KILLER_ENTITY));
        }

        return false;
    }

    private boolean matchSlot(@Nullable Entity entity) {
        if (entity instanceof ServerPlayerEntity) {
            return predicate.test((PlayerEntity) entity);
        }

        return false;
    }
}
