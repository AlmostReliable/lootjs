package com.almostreliable.lootjs.kube.wrapper;

import net.minecraft.advancements.critereon.MinMaxBounds;

import java.util.List;

public class IntervalJS {
    private MinMaxBounds.Doubles bound = MinMaxBounds.Doubles.ANY;

    public IntervalJS() {}

    private IntervalJS(MinMaxBounds.Doubles bound) {
        this.bound = new MinMaxBounds.Doubles(bound.getMin(), bound.getMax());
    }

    public static MinMaxBounds.Doubles ofDoubles(Object o) {
        if (o instanceof List<?> list) {
            if (list.size() == 1) {
                return ofDoubles(list.get(0));
            }

            if (list.size() == 2) {
                Object min = list.get(0);
                Object max = list.get(1);
                if (min instanceof Number minN && max instanceof Number maxN) {
                    return MinMaxBounds.Doubles.between(minN.doubleValue(), maxN.doubleValue());
                }
            }
        }

        if (o instanceof Number) {
            return MinMaxBounds.Doubles.atLeast(((Number) o).doubleValue());
        }

        if (o instanceof MinMaxBounds<? extends Number> minMaxBounds) {
            Double min = minMaxBounds.getMin() != null ? minMaxBounds.getMin().doubleValue() : null;
            Double max = minMaxBounds.getMax() != null ? minMaxBounds.getMax().doubleValue() : null;
            return new MinMaxBounds.Doubles(min, max);
        }

        if (o instanceof IntervalJS) {
            return ((IntervalJS) o).getVanillaDoubles();
        }

        throw new IllegalArgumentException("Argument is not a MinMaxBound");
    }

    public static MinMaxBounds.Ints ofInt(Object o) {
        if (o instanceof MinMaxBounds.Ints) {
            return (MinMaxBounds.Ints) o;
        }

        MinMaxBounds.Doubles doubles = ofDoubles(o);
        Integer min = doubles.getMin() != null ? doubles.getMin().intValue() : null;
        Integer max = doubles.getMax() != null ? doubles.getMax().intValue() : null;
        return new MinMaxBounds.Ints(min, max);
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
