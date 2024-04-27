package com.almostreliable.lootjs.kube;

import com.almostreliable.lootjs.LootTableEvent;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.event.EventResult;
import net.minecraft.core.WritableRegistry;
import net.minecraft.world.level.storage.loot.LootTable;

public class LootTableEventJS extends EventJS implements LootTableEvent {
    private final WritableRegistry<LootTable> registry;

    public LootTableEventJS(WritableRegistry<LootTable> registry) {
        this.registry = registry;
    }

    @Override
    public WritableRegistry<LootTable> registry() {
        return registry;
    }

    @Override
    protected void afterPosted(EventResult result) {
        super.afterPosted(result);

        // TODO freeze somehow everything?
    }
}
