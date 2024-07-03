package com.almostreliable.lootjs.mixin.ctor_hooks;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Optional;

@Mixin(ItemPredicate.class)
public class ItemPredicateMixin {
    @Mutable @Shadow @Final private MinMaxBounds.Ints count;

    @Mutable @Shadow @Final private DataComponentPredicate components;

    @Mutable @Shadow @Final private Map<ItemSubPredicate.Type<?>, ItemSubPredicate> subPredicates;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void lootjs$setDefaults(Optional<HolderSet<Item>> items, MinMaxBounds.Ints count, DataComponentPredicate components, Map<ItemSubPredicate.Type<?>, ItemSubPredicate> subPredicates, CallbackInfo ci) {
        if (this.count == null) {
            this.count = MinMaxBounds.Ints.ANY;
        }

        if (this.components == null) {
            this.components = DataComponentPredicate.EMPTY;
        }

        if (this.subPredicates == null) {
            this.subPredicates = Map.of();
        }
    }
}
