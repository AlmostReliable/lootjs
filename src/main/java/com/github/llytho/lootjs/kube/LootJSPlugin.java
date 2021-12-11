package com.github.llytho.lootjs.kube;

import com.github.llytho.lootjs.core.LootContextType;
import dev.latvian.kubejs.KubeJSPlugin;
import dev.latvian.kubejs.script.BindingsEvent;

public class LootJSPlugin extends KubeJSPlugin {
    @Override
    public void addBindings(BindingsEvent event) {
        event.add("LootType", LootContextType.class);
    }
}
