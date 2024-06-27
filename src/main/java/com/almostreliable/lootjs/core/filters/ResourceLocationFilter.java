package com.almostreliable.lootjs.core.filters;

import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public interface ResourceLocationFilter extends Predicate<ResourceLocation> {
    record ByLocation(ResourceLocation location) implements ResourceLocationFilter {
        @Override
        public boolean test(ResourceLocation resourceLocation) {
            return location.equals(resourceLocation);
        }

        @Override
        public String toString() {
            return "Id[" + location + "]";
        }
    }

    record ByPattern(Pattern pattern) implements ResourceLocationFilter {
        @Override
        public boolean test(ResourceLocation resourceLocation) {
            return pattern.matcher(resourceLocation.toString()).matches();
        }

        @Override
        public String toString() {
            return "Pattern[" + pattern.pattern() + "]";
        }
    }

    record ByMod(String mod) implements ResourceLocationFilter {

        @Override
        public boolean test(ResourceLocation resourceLocation) {
            return resourceLocation.getNamespace().equals(mod);
        }

        @Override
        public String toString() {
            return "Mod[" + mod + "]";
        }
    }

    record Or(List<ResourceLocationFilter> filters) implements ResourceLocationFilter {
        @Override
        public boolean test(ResourceLocation resourceLocation) {
            return filters.stream().anyMatch(filter -> filter.test(resourceLocation));
        }

        @Override
        public String toString() {
            return "Or[" + StringUtils.join(filters, ", ") + "]";
        }
    }
}
