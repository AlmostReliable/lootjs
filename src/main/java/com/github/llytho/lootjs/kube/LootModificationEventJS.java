package com.github.llytho.lootjs.kube;

import com.github.llytho.lootjs.LootModificationsAPI;
import com.github.llytho.lootjs.action.CompositeAction;
import dev.latvian.kubejs.event.EventJS;
import dev.latvian.mods.rhino.util.HideFromJS;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LootModificationEventJS extends EventJS {

    private final List<CompositeLootActionBuilder> modifierBuilders = new ArrayList<>();

    public CompositeLootActionBuilder addModifier() {
        CompositeLootActionBuilder builder = new CompositeLootActionBuilder();
        modifierBuilders.add(builder);
        return builder;
    }

    @HideFromJS
    public List<CompositeLootActionBuilder> getModifierBuilders() {
        return modifierBuilders;
    }

    @Override
    protected void afterPosted(boolean result) {
        super.afterPosted(result);

        List<CompositeAction> actions = getModifierBuilders()
                .stream()
                .map(CompositeLootActionBuilder::build)
                .collect(Collectors.toList());

        for (CompositeAction action : actions) {
            LootModificationsAPI.get().addAction(action);
        }
    }
}
