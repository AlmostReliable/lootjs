package com.almostreliable.lootjs.mixin;

import com.almostreliable.lootjs.core.LootType;
import com.almostreliable.lootjs.loot.extension.LootParamsExtension;
import net.minecraft.world.level.storage.loot.LootParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LootParams.class)
public class LootParamsMixin implements LootParamsExtension {

    @Unique
    private LootType lootjs$type = LootType.UNKNOWN;

    @Override
    public LootType lootjs$getType() {
        return lootjs$type;
    }

    @Override
    public void lootjs$setType(LootType type) {
        lootjs$type = type;
    }
}
