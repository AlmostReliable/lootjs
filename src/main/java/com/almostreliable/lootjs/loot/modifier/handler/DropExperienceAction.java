package com.almostreliable.lootjs.loot.modifier.handler;

import com.almostreliable.lootjs.core.LootBucket;
import com.almostreliable.lootjs.loot.modifier.LootAction;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class DropExperienceAction implements LootAction {
    private final int amount;

    public DropExperienceAction(int amount) {
        this.amount = amount;
    }

    @Override
    public void apply(LootContext context, LootBucket loot) {
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        if (origin != null) {
            ExperienceOrb.award(context.getLevel(), origin, amount);
        }
    }
}
