package com.github.llytho.lootjs.action;

import com.github.llytho.lootjs.core.IAction;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;

public class ExplodeAction implements IAction<LootContext> {

    private final float radius;
    private final boolean fire;
    private final Explosion.Mode mode;

    public ExplodeAction(float pRadius, Explosion.Mode pMode, boolean pFire) {
        radius = pRadius;
        fire = pFire;
        mode = pMode;
    }

    @Override
    public boolean accept(LootContext pContext) {
        Vector3d origin = pContext.getParamOrNull(LootParameters.ORIGIN);
        if (origin == null) return true;
        pContext.getLevel().explode(null, origin.x, origin.y, origin.z, radius, fire, mode);
        return true;
    }
}
