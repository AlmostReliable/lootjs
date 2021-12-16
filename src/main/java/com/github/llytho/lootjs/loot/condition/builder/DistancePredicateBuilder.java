package com.github.llytho.lootjs.loot.condition.builder;

import net.minecraft.advancements.criterion.DistancePredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;

public class DistancePredicateBuilder {
    private MinMaxBounds.FloatBound x = MinMaxBounds.FloatBound.ANY;
    private MinMaxBounds.FloatBound y = MinMaxBounds.FloatBound.ANY;
    private MinMaxBounds.FloatBound z = MinMaxBounds.FloatBound.ANY;
    private MinMaxBounds.FloatBound horizontal = MinMaxBounds.FloatBound.ANY;
    private MinMaxBounds.FloatBound absolute = MinMaxBounds.FloatBound.ANY;

    public DistancePredicateBuilder x(MinMaxBounds.FloatBound bounds) {
        x = bounds;
        return this;
    }

    public DistancePredicateBuilder y(MinMaxBounds.FloatBound bounds) {
        y = bounds;
        return this;
    }

    public DistancePredicateBuilder z(MinMaxBounds.FloatBound bounds) {
        z = bounds;
        return this;
    }

    public DistancePredicateBuilder horizontal(MinMaxBounds.FloatBound bounds) {
        horizontal = bounds;
        return this;
    }

    public DistancePredicateBuilder absolute(MinMaxBounds.FloatBound bounds) {
        absolute = bounds;
        return this;
    }

    public DistancePredicate build() {
        return new DistancePredicate(x, y, z, horizontal, absolute);
    }
}
