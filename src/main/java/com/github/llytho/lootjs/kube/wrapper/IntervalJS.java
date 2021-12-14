package com.github.llytho.lootjs.kube.wrapper;

import net.minecraft.advancements.criterion.MinMaxBounds;

import javax.annotation.Nullable;

public class IntervalJS {
    @Nullable
    private Float min;
    @Nullable
    private Float max;

    public IntervalJS() {}

    private IntervalJS(IntervalJS old) {
        min = old.min;
        max = old.max;
    }

    public IntervalJS between(float min, float max) {
        return new IntervalJS().min(min).max(max);
    }

    public IntervalJS min(float value) {
        IntervalJS bound = new IntervalJS(this);
        bound.min = value;
        return bound;
    }

    public IntervalJS max(float value) {
        IntervalJS bound = new IntervalJS(this);
        bound.max = value;
        return bound;
    }

    public MinMaxBounds.IntBound getVanillaInt() {
        Integer intMin = min == null ? null : min.intValue();
        Integer intMax = max == null ? null : max.intValue();
        return new MinMaxBounds.IntBound(intMin, intMax);
    }

    public MinMaxBounds.FloatBound getVanillaFloat() {
        return new MinMaxBounds.FloatBound(min, max);
    }

    @Override
    public String toString() {
        String minStr = min == null ? "-\u221e" : String.valueOf(min);
        String maxStr = max == null ? "\u221e" : String.valueOf(max);
        return "Interval[" + minStr + ";" + maxStr + "]";
    }
}
