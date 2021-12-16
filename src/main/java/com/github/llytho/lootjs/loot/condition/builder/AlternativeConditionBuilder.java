package com.github.llytho.lootjs.loot.condition.builder;

import com.github.llytho.lootjs.kube.ConditionsContainer;
import net.minecraft.loot.conditions.Alternative;
import net.minecraft.loot.conditions.ILootCondition;

import java.util.ArrayList;
import java.util.List;

public class AlternativeConditionBuilder implements ConditionsContainer<AlternativeConditionBuilder> {

    private final List<ILootCondition> conditions = new ArrayList<>();

    public Alternative build() {
        if (conditions.isEmpty()) {
            throw new IllegalArgumentException("No conditions set for `any`");
        }

        return new Alternative(conditions.toArray(new ILootCondition[0]));
    }

    @Override
    public AlternativeConditionBuilder addCondition(ILootCondition condition) {
        conditions.add(condition);
        return this;
    }
}
