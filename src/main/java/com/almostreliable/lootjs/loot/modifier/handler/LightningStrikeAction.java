package com.almostreliable.lootjs.loot.modifier.handler;

import com.almostreliable.lootjs.core.LootBucket;
import com.almostreliable.lootjs.loot.modifier.LootAction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class LightningStrikeAction implements LootAction {
    private final boolean shouldDamageEntity;

    public LightningStrikeAction(boolean shouldDamageEntity) {
        this.shouldDamageEntity = shouldDamageEntity;
    }

    @Override
    public void apply(LootContext context, LootBucket loot) {
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        if (origin != null) {
            LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(context.getLevel());
            if (lightning != null) {
                lightning.moveTo(origin.x, origin.y, origin.z);
                if (!shouldDamageEntity) lightning.setVisualOnly(true);
                context.getLevel().addFreshEntity(lightning);
            }
        }
    }
}
