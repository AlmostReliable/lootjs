package com.almostreliable.lootjs.fabric.mixin;

import com.almostreliable.lootjs.LootJS;
import com.almostreliable.lootjs.LootModificationsAPI;
import com.almostreliable.lootjs.core.LootJSParamSets;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mixin(LootTable.class)
public abstract class LootTableMixin implements LootTableExtension {

    @Shadow
    public abstract void getRandomItemsRaw(LootContext lootContext, Consumer<ItemStack> consumer);

    @Unique
    protected ResourceLocation lootjs$lootTableId;

    @Inject(method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V", at = @At("HEAD"), cancellable = true)
    private void invokeLootModifiers(LootContext context, Consumer<ItemStack> consumer, CallbackInfo ci) {
        if(lootjs$getLootTableId() == null) {
//            LootJS.LOG.warn("Loot table id is null, something went wrong."); // TODO find a way to print better error
            return;
        }

        List<ItemStack> loot = new ArrayList<>();
        Consumer<ItemStack> stackSplitter = LootTable.createStackSplitter(loot::add);
        getRandomItemsRaw(context, stackSplitter);

        ILootContextData data = context.getParamOrNull(LootJSParamSets.DATA);
        if (data == null) {
            LootJS.LOG.debug("LootJS: No data found in context, skipping loot modifiers. Reason is probably that context was not created through LootContext$Builder");
            return;
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
