package com.almostreliable.lootjs.loot.modifier.handler;

import com.almostreliable.lootjs.loot.modifier.LootHandler;
import com.almostreliable.lootjs.core.LootBucket;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class DropExperienceAction implements LootHandler {
    private final int amount;

    public DropExperienceAction(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean apply(LootContext context, LootBucket loot) {
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        if (origin == null) return true;
        ExperienceOrb.award(context.getLevel(), origin, amount);
        return true;
    }
}
