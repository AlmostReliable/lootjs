package com.almostreliable.lootjs.mixin;

import com.almostreliable.lootjs.LootJS;
import com.almostreliable.lootjs.LootJSPlatform;
import com.almostreliable.lootjs.core.LootBucket;
import com.almostreliable.lootjs.loot.table.LootTracker;
import com.almostreliable.lootjs.loot.table.PostLootAction;
import com.almostreliable.lootjs.loot.table.PostLootActionOwner;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@Mixin(LootTable.class)
public class LootTableMixin implements PostLootActionOwner {

    @Unique
    @Nullable
    private PostLootAction postLootAction;

    @Override
    public void lootjs$setPostLootAction(PostLootAction postLootAction) {
        this.postLootAction = postLootAction;
    }

    @Nullable
    @Override
    public PostLootAction lootjs$getPostLootAction() {
        return this.postLootAction;
    }

    @ModifyVariable(method = "getRandomItemsRaw(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V", at = @At("HEAD"), argsOnly = true)
    private Consumer<ItemStack> lootjs$createLootTracker(Consumer<ItemStack> output, LootContext ctx, Consumer<ItemStack> $) {
        if (lootjs$getPostLootAction() != null) {
            return new LootTracker(output, LootJSPlatform.INSTANCE.getLootTableId((LootTable) (Object) this));
        }

        return output;
    }

    @Inject(method = "getRandomItemsRaw(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V", at = @At("TAIL"), cancellable = true)
    private void lootjs$resolveLootTracker(LootContext context, Consumer<ItemStack> output, CallbackInfo ci) {
        if (!(output instanceof LootTracker tracker)) {
            return;
        }

        var tableId = LootJSPlatform.INSTANCE.getLootTableId((LootTable) (Object) this);
        if (tracker.getTableId() != tableId) {
            LootJS.LOG.warn(
                    "LootJS: Loot table id mismatch when trying to resolve loot tracker. Expected: '{}', actual: '{}'",
                    tableId,
                    tracker.getTableId());
            return;
        }

        PostLootAction action = lootjs$getPostLootAction();
        if (action != null) {
            action.alter(context, new LootBucket(context, tracker.getLoot()));
        }

        tracker.resolve();
    }

}
