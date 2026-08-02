package com.almostreliable.lootjs.core.filters;

import net.minecraft.resources.Identifier;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public interface IdFilter extends Predicate<Identifier> {
    record ByLocation(Identifier location) implements IdFilter {
        @Override
        public boolean test(Identifier Identifier) {
            return location.equals(Identifier);
        }

        @Override
        public String toString() {
            return "Id[" + location + "]";
        }
    }

    record ByPattern(Pattern pattern) implements IdFilter {
        @Override
        public boolean test(Identifier Identifier) {
            return pattern.matcher(Identifier.toString()).matches();
        }

        @Override
        public String toString() {
            return "Pattern[" + pattern.pattern() + "]";
        }
    }

    record ByMod(String mod) implements IdFilter {

        @Override
        public boolean test(Identifier Identifier) {
            return Identifier.getNamespace().equals(mod);
        }

        @Override
        public String toString() {
            return "Mod[" + mod + "]";
        }
    }

    record Or(List<IdFilter> filters) implements IdFilter {
        @Override
        public boolean test(Identifier Identifier) {
            return filters.stream().anyMatch(filter -> filter.test(Identifier));
        }

        @Override
        public String toString() {
            return "Or[" + StringUtils.join(filters, ", ") + "]";
        }
    }
}
