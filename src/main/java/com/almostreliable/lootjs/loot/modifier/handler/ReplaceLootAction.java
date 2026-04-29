package com.almostreliable.lootjs.loot.modifier.handler;

import com.almostreliable.lootjs.core.LootBucket;
import com.almostreliable.lootjs.core.entry.LootEntry;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.loot.modifier.LootAction;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.level.storage.loot.LootContext;

public record ReplaceLootAction(ItemFilter filter, LootEntry lootEntry, boolean preserveCount,
                                DataComponentType<?>[] preserveComponentTypes)
        implements LootAction {

    @Override
    public void apply(LootContext context, LootBucket loot) {
        loot.replace(filter, lootEntry, preserveCount, preserveComponentTypes);
    }
}
