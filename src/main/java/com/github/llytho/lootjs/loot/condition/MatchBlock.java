package com.github.llytho.lootjs.loot.condition;

import net.minecraft.advancements.criterion.BlockPredicate;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class MatchBlock implements IExtendedLootCondition {

    private final BlockPredicate predicate;

    public MatchBlock(BlockPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(LootContext context) {
        Vector3d origin = context.getParamOrNull(LootParameters.ORIGIN);
        if (origin == null) return false;

        BlockPos blockPos = new BlockPos(origin.x, origin.y, origin.z);
        return predicate.matches(context.getLevel(), blockPos);
    }
}
