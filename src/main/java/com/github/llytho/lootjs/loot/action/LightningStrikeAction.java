package com.github.llytho.lootjs.loot.action;

import com.github.llytho.lootjs.core.ILootAction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class LightningStrikeAction implements ILootAction {
    private final boolean shouldDamageEntity;

    public LightningStrikeAction(boolean shouldDamageEntity) {
        this.shouldDamageEntity = shouldDamageEntity;
    }

    @Override
    public boolean accept(LootContext context) {
        Vector3d origin = context.getParamOrNull(LootParameters.ORIGIN);
        if (origin == null) return true;

        LightningBoltEntity lightning = EntityType.LIGHTNING_BOLT.create(context.getLevel());
        if (lightning != null) {
            lightning.moveTo(origin.x, origin.y, origin.z);
            if (!shouldDamageEntity) lightning.setVisualOnly(true);
            context.getLevel().addFreshEntity(lightning);
        }

        return true;
    }
}
