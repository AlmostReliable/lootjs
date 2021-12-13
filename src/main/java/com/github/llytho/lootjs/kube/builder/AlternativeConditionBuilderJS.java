package com.github.llytho.lootjs.kube.builder;

import com.github.llytho.lootjs.kube.ConditionsContainer;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.loot.conditions.Alternative;
import net.minecraft.loot.conditions.ILootCondition;

import java.util.ArrayList;
import java.util.List;

public class AlternativeConditionBuilderJS implements ConditionsContainer<AlternativeConditionBuilderJS> {

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
    public AlternativeConditionBuilderJS addCondition(ILootCondition condition) {
        conditions.add(condition);
        return this;
    }
}
