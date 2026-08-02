package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.LootJSConditions;
import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.criterion.DistancePredicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record MatchKillerDistance(DistancePredicate predicate) implements LootItemCondition {

    @Override
    public boolean test(LootContext context) {
        Entity entity = context.getOptionalParameter(LootContextParams.THIS_ENTITY);
        Entity killerEntity = context.getOptionalParameter(LootContextParams.ATTACKING_ENTITY);
        return entity != null && killerEntity != null && predicate.matches(entity.getX(),
                entity.getY(),
                entity.getZ(),
                killerEntity.getX(),
                killerEntity.getY(),
                killerEntity.getZ());

    }

    @Override
    public MapCodec<? extends LootItemCondition> codec() {
        return LootJSConditions.DISTANCE.value();
    }
}
