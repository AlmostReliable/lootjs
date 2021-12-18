package com.github.llytho.lootjs.core;

import net.minecraft.loot.LootContext;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.regex.Pattern;

public class LootModificationByTable extends AbstractLootModification {

    public final List<ResourceLocation> locations;
    public final List<Pattern> patterns;
    private final String name;


    public LootModificationByTable(String name, List<ResourceLocation> locations, List<Pattern> patterns, List<LootAction> actions) {
        super(actions);
        this.name = name;
        this.locations = locations;
        this.patterns = patterns;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean shouldExecute(LootContext context) {
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
