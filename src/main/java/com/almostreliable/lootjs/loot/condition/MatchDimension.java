package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.LootJSConditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class MatchDimension implements LootItemCondition {

    private final ResourceLocation[] dimensions;

    public MatchDimension(ResourceLocation[] dimensions) {
        this.dimensions = dimensions;
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


    @Override
    public LootItemConditionType getType() {
        return LootJSConditions.ANY_DIMENSION.value();
    }
}
