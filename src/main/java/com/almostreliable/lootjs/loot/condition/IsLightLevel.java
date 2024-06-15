package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.LootJSConditions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.phys.Vec3;

public class IsLightLevel implements LootItemCondition {

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

        BlockPos blockPos = new BlockPos((int) origin.x, (int) origin.y, (int) origin.z);
        int light = context.getLevel().getMaxLocalRawBrightness(blockPos);
        return min <= light && light <= max;
    }


    @Override
    public LootItemConditionType getType() {
        return LootJSConditions.LIGHT_LEVEL.value();
    }
}
