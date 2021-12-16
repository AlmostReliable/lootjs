package com.github.llytho.lootjs.loot.condition;

import com.github.llytho.lootjs.util.LootContextUtils;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.math.vector.Vector3d;

public class MatchPlayer implements IExtendedLootCondition {
    private final EntityPredicate predicate;

    public MatchPlayer(EntityPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(LootContext context) {
        ServerPlayerEntity player = LootContextUtils.getPlayerOrNull(context);
        Vector3d origin = context.getParamOrNull(LootParameters.ORIGIN);
        return predicate.matches(context.getLevel(), origin, player);
    }
}
