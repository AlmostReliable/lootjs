package com.github.llytho.lootjs.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

public class ModificationFilter {

    private final HashSet<ResourceLocation> locations = new HashSet<>();
    private final List<Pattern> patterns = new ArrayList<>();

    public void add(ResourceLocation location) {
        locations.add(location);
    }

    public void add(Pattern pattern) {
        patterns.add(pattern);
    }

    public void clear() {
        locations.clear();
        patterns.clear();
    }

    public boolean matches(LootContext context) {
        if (locations.contains(context.getQueriedLootTableId())) {
            return true;
        }

        String s = context.getQueriedLootTableId().toString();
        for (Pattern pattern : patterns) {
            if (pattern.matcher(s).matches()) {
                locations.add(context.getQueriedLootTableId());
                return true;
            }
        }

        return false;
    }
}
