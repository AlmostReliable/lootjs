package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.util.LootContextUtils;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class MatchPlayer implements IExtendedLootCondition {
    private final EntityPredicate predicate;

    public MatchPlayer(EntityPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(LootContext context) {
        ServerPlayer player = LootContextUtils.getPlayerOrNull(context);
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        return predicate.matches(context.getLevel(), origin, player);
    }
}
