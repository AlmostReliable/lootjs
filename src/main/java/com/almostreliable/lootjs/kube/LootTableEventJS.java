package com.almostreliable.lootjs.kube;

import com.almostreliable.lootjs.loot.LootTableEvent;
import dev.latvian.mods.kubejs.event.EventResult;
import dev.latvian.mods.kubejs.event.KubeEvent;
import net.minecraft.core.WritableRegistry;
import net.minecraft.world.level.storage.loot.LootTable;

public class LootTableEventJS extends LootTableEvent implements KubeEvent {

    public LootTableEventJS(WritableRegistry<LootTable> registry) {
        super(registry);
    }

    @Override
    public void afterPosted(EventResult result) {
        // TODO freeze somehow everything?
    }
}
