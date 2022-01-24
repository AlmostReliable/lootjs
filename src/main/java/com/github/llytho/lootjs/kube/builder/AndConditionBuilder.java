package com.github.llytho.lootjs.kube.builder;

import com.github.llytho.lootjs.kube.ConditionsContainer;
import com.github.llytho.lootjs.loot.condition.AndCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.ArrayList;
import java.util.List;

public class AndConditionBuilder implements ConditionsContainer<AndConditionBuilder> {

    private final List<LootItemCondition> conditions = new ArrayList<>();

    public AndCondition build() {
        if (conditions.isEmpty()) {
            throw new IllegalArgumentException("No conditions set for `and`");
        }

        return new AndCondition(conditions.toArray(new LootItemCondition[0]));
    }

    @Override
    public AndConditionBuilder addCondition(LootItemCondition condition) {
        conditions.add(condition);
        return this;
    }
}
