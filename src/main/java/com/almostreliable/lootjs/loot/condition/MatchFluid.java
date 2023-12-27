package com.almostreliable.lootjs.loot.condition;

import net.minecraft.advancements.critereon.FluidPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class MatchFluid implements IExtendedLootCondition {

    private final FluidPredicate predicate;

    public MatchFluid(FluidPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(LootContext context) {
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        if (origin == null) return false;

        BlockPos blockPos = new BlockPos((int) origin.x, (int) origin.y, (int) origin.z);
        return predicate.matches(context.getLevel(), blockPos);
    }
}
