package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.LootJSConditions;
import net.minecraft.advancements.critereon.DistancePredicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class MatchKillerDistance implements LootItemCondition {

    private final DistancePredicate predicate;

    public MatchKillerDistance(DistancePredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(LootContext context) {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        Entity killerEntity = context.getParamOrNull(LootContextParams.ATTACKING_ENTITY);
        return entity != null && killerEntity != null && predicate.matches(entity.getX(),
                entity.getY(),
                entity.getZ(),
                killerEntity.getX(),
                killerEntity.getY(),
                killerEntity.getZ());

    }

    @Override
    public LootItemConditionType getType() {
        return LootJSConditions.DISTANCE.value();
    }
}
