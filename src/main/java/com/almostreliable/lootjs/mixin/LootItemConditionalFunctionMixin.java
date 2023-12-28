package com.almostreliable.lootjs.mixin;

import com.almostreliable.lootjs.loot.LootConditionList;
import com.almostreliable.lootjs.loot.extension.LootItemFunctionExtension;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(LootItemConditionalFunction.class)
public class LootItemConditionalFunctionMixin implements LootItemFunctionExtension {
    @Mutable @Shadow @Final protected List<LootItemCondition> predicates;
    @Mutable @Shadow @Final private Predicate<LootContext> compositePredicates;

    @Override
    public LootItemFunction lootjs$when(Consumer<LootConditionList> consumer) {
        var conditions = new LootConditionList(this.predicates);
        consumer.accept(conditions);
        this.predicates = conditions.getElements();
        this.compositePredicates = conditions;
        return (LootItemFunction) this;
    }
}
