package com.almostreliable.lootjs.mixin;

import com.almostreliable.lootjs.loot.extension.LootPoolExtension;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

@Mixin(LootPool.class)
public class LootPoolMixin implements LootPoolExtension {
    @Mutable @Shadow @Final private List<LootPoolEntryContainer> entries;
    @Mutable @Shadow @Final private List<LootItemCondition> conditions;
    @Mutable @Shadow @Final private Predicate<LootContext> compositeCondition;
    @Mutable @Shadow @Final private List<LootItemFunction> functions;
    @Mutable @Shadow @Final private BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;
    @Mutable @Shadow private NumberProvider rolls;
    @Mutable @Shadow private NumberProvider bonusRolls;


    @Override
    public List<LootPoolEntryContainer> lootjs$getEntries() {
        return this.entries;
    }

    @Override
    public void lootjs$setEntries(List<LootPoolEntryContainer> entries) {
        this.entries = entries;
    }

    @Override
    public List<LootItemCondition> lootjs$getConditions() {
        return this.conditions;
    }

    @Override
    public void lootjs$setConditions(List<LootItemCondition> conditions) {
        this.conditions = conditions;
        this.compositeCondition = LootItemConditions.andConditions(conditions);
    }

    @Override
    public List<LootItemFunction> lootjs$getFunctions() {
        return this.functions;
    }

    @Override
    public void lootjs$setFunctions(List<LootItemFunction> functions) {
        this.functions = functions;
        this.compositeFunction = LootItemFunctions.compose(functions);
    }

    @Override
    public NumberProvider lootjs$getRolls() {
        return this.rolls;
    }

    @Override
    public void lootjs$setRolls(NumberProvider rolls) {
        this.rolls = rolls;
    }

    @Override
    public NumberProvider lootjs$getBonusRolls() {
        return this.bonusRolls;
    }

    @Override
    public void lootjs$setBonusRolls(NumberProvider bonusRolls) {
        this.bonusRolls = bonusRolls;
    }
}
