package com.github.llytho.lootjs.kube.builder;

import com.github.llytho.lootjs.kube.ConditionsContainer;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.Inverted;

public class NotConditionBuilder implements ConditionsContainer<NotConditionBuilder> {

    private ILootCondition condition = null;

    public Inverted build() {
        if (condition == null) {
            throw new IllegalArgumentException("No condition was set");
        }

        return new Inverted(condition);
    }

    @Override
    public NotConditionBuilder addCondition(ILootCondition condition) {
        if (this.condition != null) {
            throw new IllegalArgumentException("Already set a condition");
        }

        this.condition = condition;
        return this;
    }
}
