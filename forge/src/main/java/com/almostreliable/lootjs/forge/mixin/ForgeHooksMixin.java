package com.almostreliable.lootjs.forge.mixin;

import com.almostreliable.lootjs.LootModificationsAPI;
import com.almostreliable.lootjs.core.LootJSParamSets;
import com.almostreliable.lootjs.core.ILootContextData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ForgeHooks.class)
public class ForgeHooksMixin {

    @Inject(method = "modifyLoot(Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/storage/loot/LootContext;)Ljava/util/List;", at = @At("RETURN"), remap = false)
    private static void invokeActions(ResourceLocation lootTableID, List<ItemStack> loot, LootContext context, CallbackInfoReturnable<List<ItemStack>> cir) {
        ILootContextData data = context.getParamOrNull(LootJSParamSets.DATA);
        if (data == null) {
            throw new IllegalStateException(
                    "Something went wrong - LootContext has no data. Please report this for the mod LootJS");
        }

        LootModificationsAPI.invokeActions(loot, context);
    }
}
