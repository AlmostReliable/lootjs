package com.github.llytho.lootjs.filters;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public interface ResourceLocationFilter extends Predicate<ResourceLocation> {
    record Equals(ResourceLocation location) implements ResourceLocationFilter {
        @Override
        public boolean test(ResourceLocation resourceLocation) {
            return location.equals(resourceLocation);
        }
    }

    record ByPattern(Pattern pattern) implements ResourceLocationFilter {
        @Override
        public boolean test(ResourceLocation resourceLocation) {
            return pattern.matcher(resourceLocation.toString()).matches();
        }
    }
}
