package com.github.llytho.lootjs.kube;

import com.github.llytho.lootjs.core.LootContextType;
import com.github.llytho.lootjs.kube.wrapper.IntervalJS;
import dev.latvian.kubejs.KubeJSPlugin;
import dev.latvian.kubejs.script.BindingsEvent;

public class LootJSPlugin extends KubeJSPlugin {
    @Override
    public void addBindings(BindingsEvent event) {
        event.add("LootType", LootContextType.class);
        event.add("Interval", new IntervalJS());
    }
}
