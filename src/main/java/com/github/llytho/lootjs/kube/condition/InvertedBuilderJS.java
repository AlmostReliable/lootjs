package com.github.llytho.lootjs.kube.condition;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.Inverted;

public class InvertedBuilderJS implements IConditionBuilder<InvertedBuilderJS> {

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
    public InvertedBuilderJS addCondition(ILootCondition condition) {
        if (this.condition != null) {
            throw new IllegalArgumentException("Already set a condition");
        }

        this.condition = condition;
        return this;
    }
}
