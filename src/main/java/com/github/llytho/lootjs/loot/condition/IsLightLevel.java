package com.github.llytho.lootjs.loot.condition;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class IsLightLevel implements IExtendedLootCondition {

    private final int min;
    private final int max;

    public IsLightLevel(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean test(LootContext context) {
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        if (origin == null) {
            return false;
        }

        BlockPos blockPos = new BlockPos(origin.x, origin.y, origin.z);
        int light = context.getLevel().getMaxLocalRawBrightness(blockPos);
        return min <= light && light <= max;
    }
}
