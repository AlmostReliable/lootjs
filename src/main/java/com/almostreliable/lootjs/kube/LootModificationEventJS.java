package com.almostreliable.lootjs.kube;

import com.almostreliable.lootjs.loot.LootModificationEvent;
import dev.latvian.mods.kubejs.event.EventResult;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.script.ConsoleJS;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;

import java.util.Map;
import java.util.stream.Collectors;

public class LootModificationEventJS extends LootModificationEvent implements KubeEvent {

    public LootModificationEventJS(Map<Identifier, IGlobalLootModifier> modifiers) {
        super(modifiers);
    }

    @Override
    public void afterPosted(EventResult result) {
        if (!removedGlobalModifiers.isEmpty()) {
            ConsoleJS.SERVER.info("[LootJS] Removed " + removedGlobalModifiers.size() + " global loot modifiers: " +
                                  removedGlobalModifiers
                                          .stream()
                                          .map(Identifier::toString)
                                          .collect(Collectors.joining(", ")));
        }

        storeModifiers(throwable -> ConsoleJS.SERVER.error(throwable));
    }
}
