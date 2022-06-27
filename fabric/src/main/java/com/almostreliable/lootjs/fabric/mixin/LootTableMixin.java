package com.almostreliable.lootjs.fabric.mixin;

import com.almostreliable.lootjs.fabric.LootTableExtension;
import com.almostreliable.lootjs.LootModificationsAPI;
import com.almostreliable.lootjs.core.Constants;
import com.almostreliable.lootjs.fabric.core.FabricLootContextExtension;
import com.almostreliable.lootjs.core.ILootContextData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LootTable.class)
public class LootTableMixin implements LootTableExtension {

    @Unique
    protected ResourceLocation lootjs$lootTableId;

    @Inject(method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;)Ljava/util/List;", at = @At("RETURN"))
    private void invokeLootModifiers(LootContext context, CallbackInfoReturnable<List<ItemStack>> cir) {
        ILootContextData data = context.getParamOrNull(Constants.DATA);
        if (data == null) {
            throw new IllegalStateException(
                    "Something went wrong - LootContext has no data. Please report this for the mod LootJS");
        }

        ((FabricLootContextExtension) context).lootjs$setQueriedLootTableId(lootjs$lootTableId);
        LootModificationsAPI.invokeActions(cir.getReturnValue(), context);
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
