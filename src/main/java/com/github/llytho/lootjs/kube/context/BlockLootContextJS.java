package com.github.llytho.lootjs.kube.context;

import dev.latvian.kubejs.world.BlockContainerJS;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

// TODO USE IT
public class BlockLootContextJS extends LootContextJS {
    private final BlockPos blockPos;

    public BlockLootContextJS(LootContext context) {
        super(context);
        Vector3d vec = context.getParamOrNull(LootParameters.ORIGIN);
        assert vec != null;
        this.blockPos = new BlockPos(vec);
    }

    public BlockContainerJS getBlock() {
        return new BlockContainerJS(getLevel().minecraftLevel, blockPos);
    }

    public boolean isExploded() {
        return context.hasParam(LootParameters.EXPLOSION_RADIUS);
    }

    public float getExplosionRadius() {
        Float f = context.getParamOrNull(LootParameters.EXPLOSION_RADIUS);
        return f != null ? f : 0f;
    }
}
