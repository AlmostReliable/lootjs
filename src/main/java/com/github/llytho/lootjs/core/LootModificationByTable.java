package com.github.llytho.lootjs.core;

import com.github.llytho.lootjs.filters.ResourceLocationFilter;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;

public class LootModificationByTable extends AbstractLootModification {

    public final ResourceLocationFilter[] filters;

    public LootModificationByTable(String name, ResourceLocationFilter[] filters, List<ILootHandler> handlers) {
        super(name, handlers);
        this.filters = filters;
    }

    @Override
    public boolean shouldExecute(LootContext context) {
        for (ResourceLocationFilter filter : filters) {
            if (filter.test(context.getQueriedLootTableId())) {
                return true;
            }
        }
        return false;
    }
}
