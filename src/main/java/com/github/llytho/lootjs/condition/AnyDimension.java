package com.github.llytho.lootjs.condition;

import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;

import java.util.function.Predicate;

public class AnyDimension implements IExtendedLootCondition {

    private final ResourceLocation[] dimensions;

    public AnyDimension(ResourceLocation[] pDimensions) {
        this.dimensions = pDimensions;
    }

    @Override
    public boolean test(LootContext context) {
        ResourceLocation levelDimension = context.getLevel().dimension().location();
        for (ResourceLocation dimension : dimensions) {
            if (dimension.equals(levelDimension)) {
                return true;
            }
        }

        return false;
    }
}
