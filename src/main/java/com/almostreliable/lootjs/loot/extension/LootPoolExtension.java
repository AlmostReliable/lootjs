package com.almostreliable.lootjs.loot.extension;

import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.List;

public interface LootPoolExtension {

    List<LootPoolEntryContainer> lootjs$getEntries();

    void lootjs$setEntries(List<LootPoolEntryContainer> entries);

    List<LootItemCondition> lootjs$getConditions();

    void lootjs$setConditions(List<LootItemCondition> conditions);

    List<LootItemFunction> lootjs$getFunctions();

    void lootjs$setFunctions(List<LootItemFunction> functions);

    NumberProvider lootjs$getRolls();

    void lootjs$setRolls(NumberProvider rolls);

    NumberProvider lootjs$getBonusRolls();

    void lootjs$setBonusRolls(NumberProvider bonusRolls);
}
