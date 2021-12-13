package com.github.llytho.lootjs.loot.condition;

import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class IsLightLevel implements IExtendedLootCondition {

    private final int min;
    private final int max;

    public IsLightLevel(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean test(LootContext context) {
        Vector3d origin = context.getParamOrNull(LootParameters.ORIGIN);
        if (origin == null) {
            return false;
        }

        BlockPos blockPos = new BlockPos(origin.x, origin.y, origin.z);
        int light = context.getLevel().getMaxLocalRawBrightness(blockPos);
        return min <= light && light <= max;
    }
}
