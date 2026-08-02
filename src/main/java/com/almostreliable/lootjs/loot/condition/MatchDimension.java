package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.LootJSConditions;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record MatchDimension(Identifier[] dimensions) implements LootItemCondition {

    @Override
    public boolean test(LootContext context) {
        Identifier levelDimension = context.getLevel().dimension().identifier();
        for (Identifier dimension : dimensions) {
            if (dimension.equals(levelDimension)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Identifier[] dimensions() {
        return dimensions.clone();
    }

    @Override
    public MapCodec<? extends LootItemCondition> codec() {
        return LootJSConditions.ANY_DIMENSION.value();
    }
}
