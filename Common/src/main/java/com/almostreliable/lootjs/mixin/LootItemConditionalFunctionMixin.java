package com.almostreliable.lootjs.mixin;

import com.almostreliable.lootjs.loot.table.LootConditionList;
import com.almostreliable.lootjs.loot.table.LootItemConditionalFunctionExtension;
import com.almostreliable.lootjs.loot.table.LootItemFunctionExtension;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(LootItemConditionalFunction.class)
public class LootItemConditionalFunctionMixin implements LootItemConditionalFunctionExtension,
                                                         LootItemFunctionExtension {
    @Mutable @Shadow @Final protected LootItemCondition[] predicates;

    @Mutable @Shadow @Final private Predicate<LootContext> compositePredicates;

    @Override
    public LootItemCondition[] lootjs$getConditions() {
        return this.predicates;
    }

    @Override
    public void lootjs$setConditions(LootItemCondition[] conditions) {
        this.predicates = conditions;
        this.compositePredicates = LootItemConditions.andConditions(conditions);
    }

    @Override
    public LootItemFunction lootjs$when(Consumer<LootConditionList> consumer) {
        LootConditionList l = new LootConditionList(predicates);
        consumer.accept(l);
        this.lootjs$setConditions(l.createVanillaArray());
        return (LootItemFunction) this;
    }
}
