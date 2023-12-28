package com.almostreliable.lootjs.loot.extension;

import com.almostreliable.lootjs.loot.LootConditionList;
import com.almostreliable.lootjs.loot.LootEntryList;
import com.almostreliable.lootjs.loot.LootFunctionList;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.List;

public interface LootPoolExtension {

    static LootPoolExtension cast(LootPool pool) {
        return (LootPoolExtension) pool;
    }

    LootEntryList lootjs$createEntryList();

    void lootjs$setEntries(List<LootPoolEntryContainer> entries);

    LootConditionList lootjs$createConditionList();

    LootFunctionList lootjs$createFunctionList();

    NumberProvider lootjs$getRolls();

    void lootjs$setRolls(NumberProvider rolls);

    NumberProvider lootjs$getBonusRolls();

    void lootjs$setBonusRolls(NumberProvider bonusRolls);
}
