package com.almostreliable.lootjs.loot.extension;

import com.almostreliable.lootjs.loot.LootConditionList;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

import java.util.function.Consumer;

@RemapPrefixForJS("lootjs$")
public interface LootItemFunctionExtension {

    LootItemFunction lootjs$when(Consumer<LootConditionList> consumer);
}
