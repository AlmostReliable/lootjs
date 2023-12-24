package com.almostreliable.lootjs.kube.wrappers;

import com.almostreliable.lootjs.LootJS;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatePropsPredicateWrapper {
    public static StatePropertiesPredicate ofStatePropertiesPredicate(@Nullable Object o) {
        if (o instanceof StatePropertiesPredicate s) {
            return s;
        }

        if (!(o instanceof Map<?, ?> map)) {
            throw new IllegalArgumentException("Invalid object for state properties. Given type: " +
                                               (o == null ? "NULL" : o.getClass().getName()));
        }

        List<StatePropertiesPredicate.PropertyMatcher> propertyMatchers = new ArrayList<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!(entry.getKey() instanceof String propertyName)) {
                continue;
            }

            Object unknownValue = entry.getValue();
            if (unknownValue instanceof Map<?, ?> minMaxValue) {
                String min = getSafePropertyValue(minMaxValue.get("min"));
                String max = getSafePropertyValue(minMaxValue.get("max"));

                var p = new StatePropertiesPredicate.RangedPropertyMatcher(propertyName, min, max);
                propertyMatchers.add(p);
                continue;
            }

            String value = getSafePropertyValue(unknownValue);
            if (value == null) {
                LootJS.LOG.warn(
                        "Can't validate value '" + unknownValue + "' for state properties. Skipping current property!");
                continue;
            }

            var p = new StatePropertiesPredicate.ExactPropertyMatcher(propertyName, value);
            propertyMatchers.add(p);
        }

        return new StatePropertiesPredicate(propertyMatchers);
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
