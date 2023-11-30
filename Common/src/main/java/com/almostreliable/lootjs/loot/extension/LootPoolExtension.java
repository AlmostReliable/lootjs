package com.almostreliable.lootjs.loot.extension;

import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public interface LootPoolExtension {

    LootPoolEntryContainer[] lootjs$getEntries();

    void lootjs$setEntries(LootPoolEntryContainer[] entries);

    LootItemCondition[] lootjs$getConditions();

    void lootjs$setConditions(LootItemCondition[] conditions);

    LootItemFunction[] lootjs$getFunctions();

    void lootjs$setFunctions(LootItemFunction[] functions);

    NumberProvider lootjs$getRolls();

    void lootjs$setRolls(NumberProvider rolls);

    NumberProvider lootjs$getBonusRolls();

    void lootjs$setBonusRolls(NumberProvider bonusRolls);
}
