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
            Double min = minMaxBounds.getMin() != null ? minMaxBounds.getMin().doubleValue() : null;
            Double max = minMaxBounds.getMax() != null ? minMaxBounds.getMax().doubleValue() : null;
            return new MinMaxBounds.Doubles(min, max);
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
            Integer min = minMaxBounds.getMin() != null ? minMaxBounds.getMin().intValue() : null;
            Integer max = minMaxBounds.getMax() != null ? minMaxBounds.getMax().intValue() : null;
            return new MinMaxBounds.Ints(min, max);
        }

        LootJS.LOG.warn("Failed creating bounds, got: " + o);
        return MinMaxBounds.Ints.exactly(Integer.MAX_VALUE);
    }
}
