package com.almostreliable.lootjs.kube;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface LootJSEvent {
    EventGroup GROUP = EventGroup.of("LootJS");
    EventHandler MODIFIERS = GROUP.server("modifiers", () -> LootModificationEventJS.class);
}
