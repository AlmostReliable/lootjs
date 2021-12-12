package com.github.llytho.lootjs.mixin;

import com.github.llytho.lootjs.LootModificationsAPI;
import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootContextData;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ForgeHooks.class)
public class ForgeHooksMixin {

    @Inject(method = "modifyLoot(Lnet/minecraft/util/ResourceLocation;Ljava/util/List;Lnet/minecraft/loot/LootContext;)Ljava/util/List;", at = @At("RETURN"), remap = false)
    private static void invokeActions(ResourceLocation lootTableID, List<ItemStack> loot, LootContext context, CallbackInfoReturnable<List<ItemStack>> cir) {
        ILootContextData data = context.getParamOrNull(Constants.DATA);
        if (data == null) {
            throw new IllegalStateException(
                    "Something went wrong - LootContext has no data. Please report this for the mod LootJS");
        }

        LootModificationsAPI.get().invokeActions(loot, context);
    }
}
