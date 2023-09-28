package com.almostreliable.lootjs.fabric.mixin;

import com.almostreliable.lootjs.loot.table.LootTableExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiFunction;

@Mixin(LootTable.class)
public class LootTableMixin implements LootTableExtension {

    @Mutable @Shadow @Final public LootPool[] pools;
    @Mutable @Shadow @Final public LootItemFunction[] functions;
    @Mutable @Shadow @Final LootContextParamSet paramSet;
    @Mutable @Shadow @Final @Nullable ResourceLocation randomSequence;
    @Mutable @Shadow @Final private BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;


    @Override
    public List<LootPool> lootjs$getPools() {
        return List.of(this.pools);
    }

    @Override
    public void lootjs$setPools(List<LootPool> pools) {
        this.pools = pools.toArray(new LootPool[0]);
    }

    @Override
    public LootItemFunction[] lootjs$getFunctions() {
        return this.functions;
    }

    @Override
    public void lootjs$setFunctions(LootItemFunction[] functions) {
        this.functions = functions;
        this.compositeFunction = LootItemFunctions.compose(functions);
    }

    @Override
    public void lootjs$setRandomSequence(@Nullable ResourceLocation randomSequence) {
        this.randomSequence = randomSequence;
    }

    @Override
    @Nullable
    public ResourceLocation lootjs$getRandomSequence() {
        return this.randomSequence;
    }

    @Override
    public void lootjs$setParamSet(LootContextParamSet paramSet) {
        this.paramSet = paramSet;
    }

    @Override
    public LootContextParamSet lootjs$getParamSet() {
        return this.paramSet;
    }
}
