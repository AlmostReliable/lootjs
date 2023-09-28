package com.almostreliable.lootjs.fabric.mixin;

import com.almostreliable.lootjs.LootJS;
import com.almostreliable.lootjs.fabric.LootTableIdOwner;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(LootTable.Builder.class)
public class LootTableBuilderMixin implements LootTableIdOwner {
    @Unique
    @Nullable
    protected ResourceLocation lootjs$lootTableId;

    @Inject(method = "build", at = @At("RETURN"))
    private void lootjs$onBuild(CallbackInfoReturnable<LootTable> cir) {
        if(lootjs$lootTableId == null) {
            LootJS.LOG.warn("LootJS: LootTable.Builder.build() called without setting loot table id. This may be a bug in a mod that adds loot tables. Please report it to the mod author.");
            return;
        }

        if(cir.getReturnValue() instanceof LootTableIdOwner lte) {
            lte.lootjs$setLootTableId(lootjs$lootTableId);
        }
    }

    @Override
    public ResourceLocation lootjs$getLootTableId() {
        return lootjs$lootTableId;
    }

    @Override
    public void lootjs$setLootTableId(ResourceLocation id) {
        lootjs$lootTableId = id;
    }
}
