package com.github.llytho.lootjs.kube.wrapper;

import net.minecraft.advancements.critereon.MinMaxBounds;

public class IntervalJS {
    private MinMaxBounds.Doubles bound = MinMaxBounds.Doubles.ANY;

    public IntervalJS() {}

    private IntervalJS(MinMaxBounds.Doubles bound) {
        this.bound = new MinMaxBounds.Doubles(bound.getMin(), bound.getMax());
    }

    public static MinMaxBounds.Doubles ofDoubles(Object o) {
        if (o instanceof Number) {
            return MinMaxBounds.Doubles.atLeast(((Number) o).doubleValue());
        }

        if (o instanceof MinMaxBounds.Doubles) {
            return (MinMaxBounds.Doubles) o;
        }

        if (o instanceof IntervalJS) {
            return ((IntervalJS) o).getVanillaDoubles();
        }

        throw new IllegalArgumentException("Argument is not a MinMaxBound");
    }

    public static MinMaxBounds.Ints ofInt(Object o) {
        if (o instanceof Number) {
            return MinMaxBounds.Ints.atLeast(((Number) o).intValue());
        }

        if (o instanceof MinMaxBounds.Ints) {
            return (MinMaxBounds.Ints) o;
        }

        if (o instanceof IntervalJS) {
            return ((IntervalJS) o).getVanillaInt();
        }

        throw new IllegalArgumentException("Argument is not a MinMaxBound");
    }

    public boolean matches(double value) {
        return bound.matches(value);
    }

    public boolean matchesSqr(double value) {
        return bound.matchesSqr(value);
    }

    public IntervalJS between(double min, double max) {
        return new IntervalJS().min(min).max(max);
    }

    public IntervalJS min(double min) {
        return new IntervalJS(new MinMaxBounds.Doubles(min, bound.getMax()));
    }

    public IntervalJS max(double max) {
        return new IntervalJS(new MinMaxBounds.Doubles(bound.getMin(), max));
    }

    public MinMaxBounds.Ints getVanillaInt() {
        Integer intMin = bound.getMin() == null ? null : bound.getMin().intValue();
        Integer intMax = bound.getMax() == null ? null : bound.getMax().intValue();
        return new MinMaxBounds.Ints(intMin, intMax);
    }

    public MinMaxBounds.Doubles getVanillaDoubles() {
        return new MinMaxBounds.Doubles(bound.getMin(), bound.getMax());
    }

    @Override
    public String toString() {
        String minStr = bound.getMin() == null ? "-\u221e" : String.valueOf(bound.getMin());
        String maxStr = bound.getMax() == null ? "\u221e" : String.valueOf(bound.getMax());
        return "Interval[" + minStr + ";" + maxStr + "]";
    }
}
