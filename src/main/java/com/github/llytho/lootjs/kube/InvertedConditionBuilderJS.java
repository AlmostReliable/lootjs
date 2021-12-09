package com.github.llytho.lootjs.kube;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.Inverted;

public class InvertedConditionBuilderJS implements IConditionBuilder<InvertedConditionBuilderJS> {

    private ILootCondition condition = null;

    @HideFromJS
    public Inverted build() {
        if (condition == null) {
            throw new IllegalArgumentException("No condition was set");
        }

        return new Inverted(condition);
    }

    @Override
    @HideFromJS
    public InvertedConditionBuilderJS addCondition(ILootCondition pCondition) {
        if (condition != null) {
            throw new IllegalArgumentException("Already set a condition");
        }

        condition = pCondition;
        return this;
    }
}
