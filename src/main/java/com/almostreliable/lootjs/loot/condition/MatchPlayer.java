package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.LootJSConditions;
import com.almostreliable.lootjs.util.LootContextUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;

public record MatchPlayer(EntityPredicate predicate) implements LootItemCondition {

    @Override
    public boolean test(LootContext context) {
        ServerPlayer player = LootContextUtils.getPlayerOrNull(context);
        Vec3 origin = context.getOptionalParameter(LootContextParams.ORIGIN);
        return predicate.matches(context.getLevel(), origin, player);
    }

    @Override
    public MapCodec<? extends LootItemCondition> codec() {
        return LootJSConditions.MATCH_PLAYER.value();
    }
}
