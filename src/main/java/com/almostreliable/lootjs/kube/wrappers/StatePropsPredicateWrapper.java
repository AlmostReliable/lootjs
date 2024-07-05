package com.almostreliable.lootjs.kube.wrappers;

import com.almostreliable.lootjs.LootJS;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class StatePropsPredicateWrapper {

    public static StatePropertiesPredicate of(@Nullable Object o) {
        if (o instanceof StatePropertiesPredicate s) {
            return s;
        }

        if (!(o instanceof Map<?, ?> map)) {
            throw new IllegalArgumentException("Invalid object for state properties. Given type: " +
                                               (o == null ? "NULL" : o.getClass().getName()));
        }

        List<StatePropertiesPredicate.PropertyMatcher> props = new ArrayList<>();
        loadMatchers(map, props::add);
        return new StatePropertiesPredicate(props);
    }

    private static void loadMatchers(Map<?, ?> map, Consumer<StatePropertiesPredicate.PropertyMatcher> onAdd) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!(entry.getKey() instanceof String propertyName)) {
                continue;
            }

            Object unknownValue = entry.getValue();
            if (unknownValue instanceof Map<?, ?> minMaxValue) {
                var min = Optional.ofNullable(getSafePropertyValue(minMaxValue.get("min")));
                var max = Optional.ofNullable(getSafePropertyValue(minMaxValue.get("max")));

                var matcher = new StatePropertiesPredicate.RangedMatcher(min, max);
                onAdd.accept(new StatePropertiesPredicate.PropertyMatcher(propertyName, matcher));
                continue;
            }

            String value = getSafePropertyValue(unknownValue);
            if (value == null) {
                LootJS.LOG.warn(
                        "Can't validate value '" + unknownValue + "' for state properties. Skipping current property!");
                continue;
            }

            var matcher = new StatePropertiesPredicate.ExactMatcher(value);
            onAdd.accept(new StatePropertiesPredicate.PropertyMatcher(propertyName, matcher));
        }
    }

    @Nullable
    private static String getSafePropertyValue(@Nullable Object o) {
        if (o instanceof String s) {
            return s;
        }

        if (o instanceof Boolean b) {
            return Boolean.toString(b);
        }

        if (o instanceof Number n) {
            return Integer.toString(n.intValue());
        }

        return null;
    }
}
