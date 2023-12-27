package com.almostreliable.lootjs.kube.wrappers;

import com.almostreliable.lootjs.LootJS;
import net.minecraft.advancements.critereon.MinMaxBounds;

import javax.annotation.Nullable;
import java.util.List;

public class MinMaxBoundsWrapper {
    @Nullable
    private static Number saveNumber(@Nullable Object o) {
        if (o instanceof Number n) {
            return n;
        }

        return null;
    }

    public static MinMaxBounds.Doubles ofMinMaxDoubles(Object o) {
        if (o instanceof String s && s.equalsIgnoreCase("any")) {
            return MinMaxBounds.Doubles.ANY;
        }

        if (o instanceof List<?> list) {
            if (list.size() == 1) {
                return ofMinMaxDoubles(list.get(0));
            }

            if (list.size() == 2) {
                var min = saveNumber(list.get(0));
                var max = saveNumber(list.get(1));
                if (min != null && max != null) {
                    return MinMaxBounds.Doubles.between(min.doubleValue(), max.doubleValue());
                }
            }
        }

        if (o instanceof Number n) {
            return MinMaxBounds.Doubles.exactly(n.doubleValue());
        }

        if (o instanceof MinMaxBounds<? extends Number> minMaxBounds) {
            var min = minMaxBounds.min().map(Number::doubleValue);
            var max = minMaxBounds.max().map(Number::doubleValue);
            return new MinMaxBounds.Doubles(min, max, min.map(d -> d * d), max.map(d -> d * d));
        }


        LootJS.LOG.warn("Failed creating bounds, got: " + o);
        return MinMaxBounds.Doubles.exactly(Double.MAX_VALUE);
    }

    public static MinMaxBounds.Ints ofMinMaxInt(@Nullable Object o) {
        if (o instanceof String s && s.equalsIgnoreCase("any")) {
            return MinMaxBounds.Ints.ANY;
        }

        if (o instanceof List<?> list) {
            if (list.size() == 1) {
                return ofMinMaxInt(list.get(0));
            }

            if (list.size() == 2) {
                var min = saveNumber(list.get(0));
                var max = saveNumber(list.get(1));
                if (min != null && max != null) {
                    return MinMaxBounds.Ints.between(min.intValue(), max.intValue());
                }
            }
        }

        if (o instanceof Number n) {
            return MinMaxBounds.Ints.exactly(n.intValue());
        }

        if (o instanceof MinMaxBounds<? extends Number> minMaxBounds) {
            var min = minMaxBounds.min().map(Number::intValue);
            var max = minMaxBounds.max().map(Number::intValue);
            return new MinMaxBounds.Ints(min, max, min.map(i -> ((long) i * i)), max.map(i -> ((long) i * i)));
        }

        LootJS.LOG.warn("Failed creating bounds, got: " + o);
        return MinMaxBounds.Ints.exactly(Integer.MAX_VALUE);
    }
}
