package com.almostreliable.lootjs.fabric.mixin;

import com.almostreliable.lootjs.LootModificationsAPI;
import com.almostreliable.lootjs.fabric.core.FabricLootContextExtension;
import com.almostreliable.lootjs.fabric.kubejs.LootConsumer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(value = LootTable.class, priority = 1337)
public class LootTablePostMixin {

    @Inject(method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V", at = @At("TAIL"))
    private void lootjs$resolveLootConsumer(LootContext context, Consumer<ItemStack> consumer, CallbackInfo ci) {
        LootConsumer lootConsumer = ((FabricLootContextExtension) context).lootjs$getLootConsumer();
        if (lootConsumer != null) {
            LootModificationsAPI.invokeActions(lootConsumer.getLoot(), context);
            lootConsumer.resolve();
        }
    }
}
