package com.github.llytho.lootjs.kube.builder;

import com.github.llytho.lootjs.kube.ConditionsContainer;
import com.github.llytho.lootjs.loot.condition.OrCondition;
import net.minecraft.loot.conditions.ILootCondition;

import java.util.ArrayList;
import java.util.List;

public class OrConditionBuilder implements ConditionsContainer<OrConditionBuilder> {

    private final List<ILootCondition> conditions = new ArrayList<>();

    public OrCondition build() {
        if (conditions.isEmpty()) {
            throw new IllegalArgumentException("No conditions set for `or`");
        }

        return new OrCondition(conditions.toArray(new ILootCondition[0]));
    }

    @Override
    public OrConditionBuilder addCondition(ILootCondition condition) {
        conditions.add(condition);
        return this;
    }
}
