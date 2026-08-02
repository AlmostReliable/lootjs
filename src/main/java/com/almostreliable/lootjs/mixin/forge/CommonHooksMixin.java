package com.almostreliable.lootjs.mixin.forge;

import com.almostreliable.lootjs.LootModificationsAPI;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.neoforged.neoforge.common.CommonHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommonHooks.class)
public class CommonHooksMixin {

    @Inject(method = "modifyLoot(Lnet/minecraft/resources/Identifier;Lit/unimi/dsi/fastutil/objects/ObjectArrayList;Lnet/minecraft/world/level/storage/loot/LootContext;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;", at = @At("RETURN"), remap = false)
    private static void invokeActions(Identifier lootTableID, ObjectArrayList<ItemStack> loot, LootContext context, CallbackInfoReturnable<ObjectArrayList<ItemStack>> cir) {
        LootModificationsAPI.invokeActions(loot, context);
    }
}
