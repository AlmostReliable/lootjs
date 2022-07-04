package com.almostreliable.lootjs.fabric.mixin;

import com.almostreliable.lootjs.LootModificationsAPI;
import com.almostreliable.lootjs.core.Constants;
import com.almostreliable.lootjs.core.ILootContextData;
import com.almostreliable.lootjs.fabric.LootTableExtension;
import com.almostreliable.lootjs.fabric.core.FabricLootContextExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mixin(LootTable.class)
public abstract class LootTableMixin implements LootTableExtension {

    @Shadow
    public abstract void getRandomItemsRaw(LootContext lootContext, Consumer<ItemStack> consumer);

    @Unique
    protected ResourceLocation lootjs$lootTableId;

    @Inject(method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V", at = @At("RETURN"), cancellable = true)
    private void invokeLootModifiers(LootContext context, Consumer<ItemStack> consumer, CallbackInfo ci) {
        List<ItemStack> loot = new ArrayList<>();
        Consumer<ItemStack> stackSplitter = LootTable.createStackSplitter(loot::add);
        getRandomItemsRaw(context, stackSplitter);

        ILootContextData data = context.getParamOrNull(Constants.DATA);
        if (data == null) {
            throw new IllegalStateException(
                    "Something went wrong - LootContext has no data. Please report this for the mod LootJS");
        }

        ((FabricLootContextExtension) context).lootjs$setQueriedLootTableId(lootjs$lootTableId);
        LootModificationsAPI.invokeActions(loot, context);

        for (ItemStack stack : loot) {
            consumer.accept(stack);
        }
        ci.cancel();
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
