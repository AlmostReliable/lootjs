package com.almostreliable.lootjs.fabric.mixin;

import com.almostreliable.lootjs.LootModificationsAPI;
import com.almostreliable.lootjs.fabric.core.FabricLootContextExtension;
import com.almostreliable.lootjs.fabric.kubejs.LootConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(value = LootTable.class, priority = 1337)
public class LootTablePostMixin {

    @Shadow @Final @Nullable ResourceLocation randomSequence;

    @Inject(method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V", at = @At("TAIL"))
    private void lootjs$resolveLootConsumer(LootContext context, Consumer<ItemStack> consumer, CallbackInfo ci) {
        LootConsumer lootConsumer = ((FabricLootContextExtension) context).lootjs$getLootConsumer();
        if (lootConsumer != null) {
            LootModificationsAPI.invokeActions(lootConsumer.getLoot(), context);
            lootConsumer.resolve();
        }
    }

    @Inject(method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;JLjava/util/function/Consumer;)V", at = @At("TAIL"))
    private void lootjs$resolveLootConsumer(LootParams params, long seed, Consumer<ItemStack> consumer, CallbackInfo ci) {
        LootConsumer lootConsumer = ((FabricLootContextExtension) params).lootjs$getLootConsumer();
        if (lootConsumer != null) {
            LootContext ctx = new LootContext.Builder(params)
                    .withOptionalRandomSeed(seed)
                    .create(randomSequence); // meh ...
            LootModificationsAPI.invokeActions(lootConsumer.getLoot(), ctx);
            lootConsumer.resolve();
        }
    }
}
