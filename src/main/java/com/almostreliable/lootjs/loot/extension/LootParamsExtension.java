package com.almostreliable.lootjs.loot.extension;

import com.almostreliable.lootjs.core.LootType;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;


@RemapPrefixForJS("lootjs$")
public interface LootParamsExtension {

    LootType lootjs$getType();

    void lootjs$setType(LootType type);
}
