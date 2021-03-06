package com.almostreliable.lootjs.loot.action;

import com.almostreliable.lootjs.core.ILootAction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class LightningStrikeAction implements ILootAction {
    private final boolean shouldDamageEntity;

    public LightningStrikeAction(boolean shouldDamageEntity) {
        this.shouldDamageEntity = shouldDamageEntity;
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        if (origin == null) return true;

        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(context.getLevel());
        if (lightning != null) {
            lightning.moveTo(origin.x, origin.y, origin.z);
            if (!shouldDamageEntity) lightning.setVisualOnly(true);
            context.getLevel().addFreshEntity(lightning);
        }

        return true;
    }
}
