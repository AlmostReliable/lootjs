package com.github.llytho.lootjs.kube.builder;

import com.github.llytho.lootjs.kube.ConditionsContainer;
import com.github.llytho.lootjs.loot.condition.AndCondition;
import net.minecraft.loot.conditions.ILootCondition;

import java.util.ArrayList;
import java.util.List;

public class AndConditionBuilder implements ConditionsContainer<AndConditionBuilder> {

    private final List<ILootCondition> conditions = new ArrayList<>();

    public AndCondition build() {
        if (conditions.isEmpty()) {
            throw new IllegalArgumentException("No conditions set for `and`");
        }

        return new AndCondition(conditions.toArray(new ILootCondition[0]));
    }

    @Override
    public AndConditionBuilder addCondition(ILootCondition condition) {
        conditions.add(condition);
        return this;
    }
}
