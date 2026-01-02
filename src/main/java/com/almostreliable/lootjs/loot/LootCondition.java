package com.almostreliable.lootjs.loot;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class LootCondition implements LootConditionsContainer<LootItemCondition> {

    @Override
    @HideFromJS
    public LootItemCondition addCondition(LootItemCondition condition) {
        return condition;
    }
}
