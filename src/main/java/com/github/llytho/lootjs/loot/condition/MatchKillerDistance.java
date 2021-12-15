package com.github.llytho.lootjs.loot.condition;

import net.minecraft.advancements.criterion.DistancePredicate;
import net.minecraft.entity.Entity;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;

public class MatchKillerDistance implements IExtendedLootCondition {

    private final DistancePredicate predicate;

    public MatchKillerDistance(DistancePredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(LootContext context) {
        Entity entity = context.getParamOrNull(LootParameters.THIS_ENTITY);
        Entity killerEntity = context.getParamOrNull(LootParameters.KILLER_ENTITY);
        return entity != null && killerEntity != null && predicate.matches(entity.getX(),
                entity.getY(),
                entity.getZ(),
                killerEntity.getX(),
                killerEntity.getY(),
                killerEntity.getZ());

    }
}
