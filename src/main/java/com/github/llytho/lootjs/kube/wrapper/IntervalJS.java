package com.github.llytho.lootjs.kube.wrapper;

import net.minecraft.advancements.criterion.MinMaxBounds;

public class IntervalJS {
    private MinMaxBounds.FloatBound bound = MinMaxBounds.FloatBound.ANY;

    public IntervalJS() {}

    private IntervalJS(MinMaxBounds.FloatBound bound) {
        this.bound = new MinMaxBounds.FloatBound(bound.getMin(), bound.getMax());
    }

    public static MinMaxBounds.FloatBound ofFloat(Object o) {
        if (o instanceof Number) {
            return new MinMaxBounds.FloatBound(((Number) o).floatValue(), null);
        }

        if (o instanceof MinMaxBounds.FloatBound) {
            return (MinMaxBounds.FloatBound) o;
        }

        if (o instanceof IntervalJS) {
            return ((IntervalJS) o).getVanillaFloat();
        }

        throw new IllegalArgumentException("Argument is not a MinMaxBound");
    }

    public static MinMaxBounds.IntBound ofInt(Object o) {
        if (o instanceof Number) {
            return new MinMaxBounds.IntBound(((Number) o).intValue(), null);
        }

        if (o instanceof MinMaxBounds.IntBound) {
            return (MinMaxBounds.IntBound) o;
        }

        if (o instanceof IntervalJS) {
            return ((IntervalJS) o).getVanillaInt();
        }

        throw new IllegalArgumentException("Argument is not a MinMaxBound");
    }

    public boolean matches(float value) {
        return bound.matches(value);
    }

    public boolean matchesSqr(double value) {
        return bound.matchesSqr(value);
    }

    public IntervalJS between(float min, float max) {
        return new IntervalJS().min(min).max(max);
    }

    public IntervalJS min(float min) {
        return new IntervalJS(new MinMaxBounds.FloatBound(min, bound.getMax()));
    }

    public IntervalJS max(float max) {
        return new IntervalJS(new MinMaxBounds.FloatBound(bound.getMin(), max));
    }

    public MinMaxBounds.IntBound getVanillaInt() {
        Integer intMin = bound.getMin() == null ? null : bound.getMin().intValue();
        Integer intMax = bound.getMax() == null ? null : bound.getMax().intValue();
        return new MinMaxBounds.IntBound(intMin, intMax);
    }

    public MinMaxBounds.FloatBound getVanillaFloat() {
        return new MinMaxBounds.FloatBound(bound.getMin(), bound.getMax());
    }

    @Override
    public String toString() {
        String minStr = bound.getMin() == null ? "-\u221e" : String.valueOf(bound.getMin());
        String maxStr = bound.getMax() == null ? "\u221e" : String.valueOf(bound.getMax());
        return "Interval[" + minStr + ";" + maxStr + "]";
    }
}
