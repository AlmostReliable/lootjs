package com.almostreliable.lootjs.core;

import com.almostreliable.lootjs.LootJSPlatform;
import com.almostreliable.lootjs.filters.ResourceLocationFilter;
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
            if (filter.test(LootJSPlatform.INSTANCE.getQueriedLootTableId(context))) {
                return true;
            }
        }
        return false;
    }
}
