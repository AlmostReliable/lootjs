package com.github.llytho.lootjs.kube.builder;

import com.github.llytho.lootjs.kube.ConditionsContainer;
import com.github.llytho.lootjs.loot.condition.OrCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.ArrayList;
import java.util.List;

public class OrConditionBuilder implements ConditionsContainer<OrConditionBuilder> {

    private final List<LootItemCondition> conditions = new ArrayList<>();

    public OrCondition build() {
        if (conditions.isEmpty()) {
            throw new IllegalArgumentException("No conditions set for `or`");
        }

        return new OrCondition(conditions.toArray(new LootItemCondition[0]));
    }

    @Override
    public OrConditionBuilder addCondition(LootItemCondition condition) {
        conditions.add(condition);
        return this;
    }
}
