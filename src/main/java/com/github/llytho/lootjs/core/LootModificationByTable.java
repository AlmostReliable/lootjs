package com.github.llytho.lootjs.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;
import java.util.regex.Pattern;

public class LootModificationByTable extends AbstractLootModification {

    public final List<ResourceLocation> locations;
    public final List<Pattern> patterns;

    public LootModificationByTable(String name, List<ResourceLocation> locations, List<Pattern> patterns, List<ILootAction> actions) {
        super(name, actions);
        this.locations = locations;
        this.patterns = patterns;
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
