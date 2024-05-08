package com.almostreliable.lootjs.kube.wrappers;

import com.almostreliable.lootjs.LootJS;
import net.minecraft.advancements.critereon.MinMaxBounds;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class MinMaxBoundsWrapper {

    private static Optional<Number> saveNumber(@Nullable Object o) {
        if (o instanceof Number n) {
            return Optional.of(n);
        }

        return Optional.empty();
    }

    public static MinMaxBounds.Doubles ofMinMaxDoubles(Object o) {
        if (o instanceof String s && s.equalsIgnoreCase("any")) {
            return MinMaxBounds.Doubles.ANY;
        }

        if (o instanceof List<?> list) {
            if (list.size() == 1) {
                return ofMinMaxDoubles(list.getFirst());
            }

            if (list.size() == 2) {
                var min = saveNumber(list.get(0)).map(Number::doubleValue);
                var max = saveNumber(list.get(1)).map(Number::doubleValue);
                return new MinMaxBounds.Doubles(min, max, min.map(d -> d * d), max.map(d -> d * d));
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

    public static MinMaxBounds.Ints ofMinMaxInt(Object o) {
        MinMaxBounds.Doubles doubles = ofMinMaxDoubles(o);
        var min = doubles.min().map(Double::intValue);
        var max = doubles.max().map(Double::intValue);
        return new MinMaxBounds.Ints(min, max, min.map(i -> ((long) i * i)), max.map(i -> ((long) i * i)));
    }
}
