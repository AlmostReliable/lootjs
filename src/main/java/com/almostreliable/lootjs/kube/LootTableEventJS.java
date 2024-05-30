package com.almostreliable.lootjs.kube;

import com.almostreliable.lootjs.LootTableEvent;
import dev.latvian.mods.kubejs.event.EventResult;
import dev.latvian.mods.kubejs.event.KubeEvent;
import net.minecraft.core.WritableRegistry;
import net.minecraft.world.level.storage.loot.LootTable;

public class LootTableEventJS implements KubeEvent, LootTableEvent {
    private final WritableRegistry<LootTable> registry;

    public LootTableEventJS(WritableRegistry<LootTable> registry) {
        this.registry = registry;
    }

    @Override
    public WritableRegistry<LootTable> registry() {
        return registry;
    }

    @Override
    public void afterPosted(EventResult result) {
        // TODO freeze somehow everything?
    }
}
