package com.github.llytho.lootjs.loot.condition;

import net.minecraft.advancements.critereon.DistancePredicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class MatchKillerDistance implements IExtendedLootCondition {

    private final DistancePredicate predicate;

    public MatchKillerDistance(DistancePredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(LootContext context) {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        Entity killerEntity = context.getParamOrNull(LootContextParams.KILLER_ENTITY);
        return entity != null && killerEntity != null && predicate.matches(entity.getX(),
                entity.getY(),
                entity.getZ(),
                killerEntity.getX(),
                killerEntity.getY(),
                killerEntity.getZ());

    }
}
