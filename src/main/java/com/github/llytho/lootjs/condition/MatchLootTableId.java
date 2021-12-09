package com.github.llytho.lootjs.condition;

import net.minecraft.loot.LootContext;
import net.minecraft.util.ResourceLocation;

import java.util.regex.Pattern;

public class MatchLootTableId implements IExtendedLootCondition {

    public final ResourceLocation[] locations;
    public final Pattern[] patterns;

    public MatchLootTableId(ResourceLocation[] pLocations, Pattern[] pPatterns) {
        this.locations = pLocations;
        this.patterns = pPatterns;
    }

    @Override
    public boolean test(LootContext pContext) {
        for (ResourceLocation location : locations) {
            if (location.equals(pContext.getQueriedLootTableId())) {
                return true;
            }
        }

        for (Pattern pattern : patterns) {
            if (pattern.matcher(pContext.getQueriedLootTableId().toString()).matches()) {
                return true;
            }
        }

        return false;
    }

}
