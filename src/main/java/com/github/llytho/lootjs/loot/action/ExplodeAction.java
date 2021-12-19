package com.github.llytho.lootjs.loot.action;

import com.github.llytho.lootjs.core.ILootAction;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;

public class ExplodeAction implements ILootAction {

    private final float radius;
    private final boolean fire;
    private final Explosion.Mode mode;

    public ExplodeAction(float radius, Explosion.Mode mode, boolean fire) {
        this.radius = radius;
        this.fire = fire;
        this.mode = mode;
    }

    @Override
    public boolean accept(LootContext context) {
        Vector3d origin = context.getParamOrNull(LootParameters.ORIGIN);
        if (origin == null) return true;
        context.getLevel().explode(null, origin.x, origin.y, origin.z, radius, fire, mode);
        return true;
    }
}
