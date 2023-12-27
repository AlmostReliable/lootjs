package com.almostreliable.lootjs.mixin;

import com.almostreliable.lootjs.core.LootType;
import com.almostreliable.lootjs.loot.extension.LootContextExtension;
import com.almostreliable.lootjs.loot.extension.LootParamsExtension;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Mixin(LootContext.class)
public class LootContextMixin implements LootContextExtension {

    @Shadow @Final private LootParams params;
    @Unique
    @Nullable
    private Map<String, Object> lootjs$data;

    @Override
    public LootContext lootjs$self() {
        return (LootContext) (Object) this;
    }

    @Override
    public Map<String, Object> lootjs$getData() {
        if (lootjs$data == null) {
            lootjs$data = new HashMap<>();
        }

        return lootjs$data;
    }

    @Override
    public LootType lootjs$getType() {
        return ((LootParamsExtension) this.params).lootjs$getType();
    }
}
