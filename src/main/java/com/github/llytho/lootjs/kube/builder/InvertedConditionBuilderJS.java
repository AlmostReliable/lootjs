package com.github.llytho.lootjs.kube.builder;

import com.github.llytho.lootjs.kube.ConditionsContainer;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.Inverted;

public class InvertedConditionBuilderJS implements ConditionsContainer<InvertedConditionBuilderJS> {

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
    public InvertedConditionBuilderJS addCondition(ILootCondition condition) {
        if (this.condition != null) {
            throw new IllegalArgumentException("Already set a condition");
        }

        this.condition = condition;
        return this;
    }
}
