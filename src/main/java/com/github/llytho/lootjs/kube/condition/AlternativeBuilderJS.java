package com.github.llytho.lootjs.kube.condition;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.loot.conditions.Alternative;
import net.minecraft.loot.conditions.ILootCondition;

import java.util.ArrayList;
import java.util.List;

public class AlternativeBuilderJS implements IConditionBuilder<AlternativeBuilderJS> {

    private final List<ILootCondition> conditions = new ArrayList<>();

    @HideFromJS
    public Alternative build() {
        if (conditions.isEmpty()) {
            throw new IllegalArgumentException("No conditions set for `any`");
        }

        return new Alternative(conditions.toArray(new ILootCondition[0]));
    }

    @Override
    @HideFromJS
    public AlternativeBuilderJS addCondition(ILootCondition pCondition) {
        conditions.add(pCondition);
        return this;
    }
}
