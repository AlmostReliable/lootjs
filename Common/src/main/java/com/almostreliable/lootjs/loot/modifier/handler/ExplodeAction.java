package com.almostreliable.lootjs.loot.modifier.handler;

import com.almostreliable.lootjs.loot.modifier.LootHandler;
import com.almostreliable.lootjs.core.LootBucket;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class ExplodeAction implements LootHandler {

    private final float radius;
    private final boolean fire;
    private final Explosion.BlockInteraction mode;

    public ExplodeAction(float radius, Explosion.BlockInteraction mode, boolean fire) {
        this.radius = radius;
        this.fire = fire;
        this.mode = mode;
    }

    @Override
    public boolean apply(LootContext context, LootBucket loot) {
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        if (origin == null) return true;
        Explosion explosion = new Explosion(context.getLevel(),
                null,
                null,
                null,
                origin.x,
                origin.y,
                origin.z,
                radius,
                fire,
                mode);
        explosion.explode();
        explosion.finalizeExplosion(true);
        return true;
    }
}
