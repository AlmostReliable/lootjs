package com.almostreliable.lootjs.fabric.mixin;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LootPool.class)
public interface LootPoolFactory {

    @Invoker("<init>")
    static LootPool create(LootPoolEntryContainer[] entries, LootItemCondition[] conditions, LootItemFunction[] functions, NumberProvider rolls, NumberProvider bonusRolls) {
        throw new AssertionError("Mixin failed to apply");
    }
}
