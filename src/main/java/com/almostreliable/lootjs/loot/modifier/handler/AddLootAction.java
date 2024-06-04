package com.almostreliable.lootjs.loot.modifier.handler;

import com.almostreliable.lootjs.core.LootBucket;
import com.almostreliable.lootjs.core.entry.LootEntry;
import com.almostreliable.lootjs.loot.modifier.LootAction;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

public class AddLootAction implements LootAction {

    private final LootPoolEntryContainer[] entries;

    public AddLootAction(LootEntry... entries) {
        this.entries = new LootPoolEntryContainer[entries.length];
        for (int i = 0; i < entries.length; i++) {
            this.entries[i] = entries[i].getVanillaEntry();
        }

    }

    @Override
    public void apply(LootContext context, LootBucket loot) {
        for (var entry : entries) {
            entry.expand(context, lootPoolEntry -> {
                lootPoolEntry.createItemStack(loot::addItem, context);
            });
        }
    }
}
