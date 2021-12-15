package com.github.llytho.lootjs.kube.builder;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.advancements.criterion.DistancePredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;

public class DistancePredicateBuilderJS {
    private MinMaxBounds.FloatBound x = MinMaxBounds.FloatBound.ANY;
    private MinMaxBounds.FloatBound y = MinMaxBounds.FloatBound.ANY;
    private MinMaxBounds.FloatBound z = MinMaxBounds.FloatBound.ANY;
    private MinMaxBounds.FloatBound horizontal = MinMaxBounds.FloatBound.ANY;
    private MinMaxBounds.FloatBound absolute = MinMaxBounds.FloatBound.ANY;

    public DistancePredicateBuilderJS x(MinMaxBounds.FloatBound bounds) {
        x = bounds;
        return this;
    }

    public DistancePredicateBuilderJS y(MinMaxBounds.FloatBound bounds) {
        y = bounds;
        return this;
    }

    public DistancePredicateBuilderJS z(MinMaxBounds.FloatBound bounds) {
        z = bounds;
        return this;
    }

    public DistancePredicateBuilderJS horizontal(MinMaxBounds.FloatBound bounds) {
        horizontal = bounds;
        return this;
    }

    public DistancePredicateBuilderJS absolute(MinMaxBounds.FloatBound bounds) {
        absolute = bounds;
        return this;
    }

    @HideFromJS
    public DistancePredicate build() {
        return new DistancePredicate(x, y, z, horizontal, absolute);
    }
}
