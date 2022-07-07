package com.almostreliable.lootjs.mixin;

import com.almostreliable.lootjs.core.LootJSParamSets;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(LootContextParamSet.class)
public class LootContextParamSetMixin {

    @Shadow @Final @Mutable private Set<LootContextParam<?>> all;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInjectConstructor(Set<LootContextParam<?>> set, Set<LootContextParam<?>> set2, CallbackInfo ci) {
       Set<LootContextParam<?>> lootJSParams = Set.of(LootJSParamSets.DATA, LootJSParamSets.RESULT_COLLECTOR);
       all = ImmutableSet.copyOf(Sets.union(all, lootJSParams));
    }
}
