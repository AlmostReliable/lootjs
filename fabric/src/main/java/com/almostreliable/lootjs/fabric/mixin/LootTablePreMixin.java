package com.almostreliable.lootjs.fabric.mixin;

import com.almostreliable.lootjs.LootJS;
import com.almostreliable.lootjs.core.ILootContextData;
import com.almostreliable.lootjs.core.LootJSParamSets;
import com.almostreliable.lootjs.fabric.LootTableIdOwner;
import com.almostreliable.lootjs.fabric.core.FabricLootContextExtension;
import com.almostreliable.lootjs.fabric.kubejs.LootConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@Mixin(value = LootTable.class, priority = 42)
public abstract class LootTablePreMixin implements LootTableIdOwner {

    @Unique
    @Nullable
    protected ResourceLocation lootjs$lootTableId;

    @ModifyVariable(method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V", at = @At("HEAD"), argsOnly = true)
    private Consumer<ItemStack> lootjs$createLootConsumer(Consumer<ItemStack> original, LootContext context, Consumer<ItemStack> consumer) {
        ResourceLocation tableId = lootjs$getLootTableId();
        if (tableId == null) {
            return original;
        }

        ILootContextData data = context.getParamOrNull(LootJSParamSets.DATA);
        if (data == null) {
            LootJS.LOG.debug(
                    "LootJS: No data found in context, skipping loot modifiers. Reason is probably that context was not created through LootContext$Builder");
            return original;
        }

        ((FabricLootContextExtension) context).lootjs$setQueriedLootTableId(tableId);
        LootConsumer lootConsumer = new LootConsumer(original);
        ((FabricLootContextExtension) context).lootjs$setLootConsumer(lootConsumer);
        return lootConsumer;
    }

    @Override
    @Nullable
    public ResourceLocation lootjs$getLootTableId() {
        return lootjs$lootTableId;
    }

    @Override
    public void lootjs$setLootTableId(ResourceLocation id) {
        lootjs$lootTableId = id;
    }
}
