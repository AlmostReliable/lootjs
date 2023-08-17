package com.almostreliable.lootjs.fabric.mixin;

import com.almostreliable.lootjs.fabric.core.FabricLootContextExtension;
import com.almostreliable.lootjs.fabric.kubejs.LootConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@Mixin(LootContext.class)
public class LootContextMixin implements FabricLootContextExtension {

    @Unique
    @Nullable
    private LootConsumer lootjs$lootConsumer;

    @Unique
    private ResourceLocation lootjs$queriedLootTableId;

    public ResourceLocation lootjs$getQueriedLootTableId() {
        return lootjs$queriedLootTableId;
    }

    public void lootjs$setQueriedLootTableId(ResourceLocation id) {
        lootjs$queriedLootTableId = id;
    }

    @Nullable
    @Override
    public LootConsumer lootjs$getLootConsumer() {
        return lootjs$lootConsumer;
    }

    @Override
    public void lootjs$setLootConsumer(LootConsumer consumer) {
        lootjs$lootConsumer = consumer;
    }
}
