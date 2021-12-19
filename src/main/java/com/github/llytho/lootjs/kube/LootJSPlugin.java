package com.github.llytho.lootjs.kube;

import com.github.llytho.lootjs.LootModificationsAPI;
import com.github.llytho.lootjs.core.LootContextType;
import com.github.llytho.lootjs.kube.wrapper.IntervalJS;
import dev.latvian.kubejs.KubeJSPlugin;
import dev.latvian.kubejs.script.BindingsEvent;
import dev.latvian.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.minecraft.advancements.criterion.MinMaxBounds;

public class LootJSPlugin extends KubeJSPlugin {

    @Override
    public void initStartup() {
        LootModificationsAPI.DEBUG_ACTION = new ConsoleDebugAction();
    }

    @Override
    public void addBindings(BindingsEvent event) {
        event.add("LootType", LootContextType.class);
        event.add("Interval", new IntervalJS());
    }

    @Override
    public void addTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        typeWrappers.register(MinMaxBounds.FloatBound.class, IntervalJS::ofFloat);
        typeWrappers.register(MinMaxBounds.IntBound.class, IntervalJS::ofInt);
    }
}
