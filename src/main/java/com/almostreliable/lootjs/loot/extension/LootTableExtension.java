package com.almostreliable.lootjs.loot.extension;

import com.almostreliable.lootjs.loot.LootFunctionList;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface LootTableExtension {

    static LootTableExtension cast(LootTable table) {
        return (LootTableExtension) table;
    }

    List<LootPool> lootjs$getPools();

    void lootjs$setPools(List<LootPool> pools);

    LootFunctionList lootjs$createFunctionList();

    void lootjs$setRandomSequence(@Nullable Identifier randomSequence);

    @Nullable
    Identifier lootjs$getRandomSequence();

    @HideFromJS
    void lootjs$recompose();
}
