package com.almostreliable.lootjs.fabric.mixin;

import com.almostreliable.lootjs.fabric.core.FabricLootContextExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LootContext.class)
public class LootContextMixin implements FabricLootContextExtension {

    @Unique
    private ResourceLocation lootjs$queriedLootTableId;

    public ResourceLocation lootjs$getQueriedLootTableId() {
        return lootjs$queriedLootTableId;
    }

    public void lootjs$setQueriedLootTableId(ResourceLocation id) {
        lootjs$queriedLootTableId = id;
    }
}
