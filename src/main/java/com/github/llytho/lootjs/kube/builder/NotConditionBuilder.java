package com.github.llytho.lootjs.kube.builder;

import com.github.llytho.lootjs.kube.ConditionsContainer;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class NotConditionBuilder implements ConditionsContainer<NotConditionBuilder> {

    private LootItemCondition condition = null;

    public InvertedLootItemCondition build() {
        if (condition == null) {
            throw new IllegalArgumentException("No condition was set");
        }

        return new InvertedLootItemCondition(condition);
    }

    @Override
    public NotConditionBuilder addCondition(LootItemCondition condition) {
        if (this.condition != null) {
            throw new IllegalArgumentException("Already set a condition");
        }

        this.condition = condition;
        return this;
    }
}
