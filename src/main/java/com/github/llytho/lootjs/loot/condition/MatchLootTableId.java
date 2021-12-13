package com.github.llytho.lootjs.loot.condition;

import net.minecraft.loot.LootContext;
import net.minecraft.util.ResourceLocation;

import java.util.regex.Pattern;

public class MatchLootTableId implements IExtendedLootCondition {

    public final ResourceLocation[] locations;
    public final Pattern[] patterns;

    public MatchLootTableId(ResourceLocation[] locations, Pattern[] patterns) {
        this.locations = locations;
        this.patterns = patterns;
    }

    @Override
    public boolean test(LootContext context) {
        for (ResourceLocation location : locations) {
            if (location.equals(context.getQueriedLootTableId())) {
                return true;
            }
        }

        for (Pattern pattern : patterns) {
            if (pattern.matcher(context.getQueriedLootTableId().toString()).matches()) {
                return true;
            }
        }

        return false;
    }

}
