package com.github.llytho.lootjs.kube.condition;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootContextData;
import dev.latvian.kubejs.item.ingredient.IngredientJS;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class MatchSlotJS implements Predicate<LootContext> {

    private final IngredientJS ingredient;
    private final EquipmentSlotType slot;

    public MatchSlotJS(EquipmentSlotType pSlot, IngredientJS pIngredient) {
        this.slot = pSlot;
        this.ingredient = pIngredient;
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
            return ingredient.testVanilla(((ServerPlayerEntity) pEntity).getItemBySlot(slot));
        }

        return false;
    }
}
