package com.github.llytho.lootjs.loot.condition.builder;

import net.minecraft.advancements.critereon.DistancePredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;

public class DistancePredicateBuilder {
    private MinMaxBounds.Doubles x = MinMaxBounds.Doubles.ANY;
    private MinMaxBounds.Doubles y = MinMaxBounds.Doubles.ANY;
    private MinMaxBounds.Doubles z = MinMaxBounds.Doubles.ANY;
    private MinMaxBounds.Doubles horizontal = MinMaxBounds.Doubles.ANY;
    private MinMaxBounds.Doubles absolute = MinMaxBounds.Doubles.ANY;

    public DistancePredicateBuilder x(MinMaxBounds.Doubles bounds) {
        x = bounds;
        return this;
    }

    public DistancePredicateBuilder y(MinMaxBounds.Doubles bounds) {
        y = bounds;
        return this;
    }

    public DistancePredicateBuilder z(MinMaxBounds.Doubles bounds) {
        z = bounds;
        return this;
    }

    public DistancePredicateBuilder horizontal(MinMaxBounds.Doubles bounds) {
        horizontal = bounds;
        return this;
    }

    public DistancePredicateBuilder absolute(MinMaxBounds.Doubles bounds) {
        absolute = bounds;
        return this;
    }

    public DistancePredicate build() {
        return new DistancePredicate(x, y, z, horizontal, absolute);
    }
}
