package com.github.llytho.lootjs.kube;

import com.github.llytho.lootjs.LootModificationsAPI;
import com.github.llytho.lootjs.core.LootContextType;
import com.github.llytho.lootjs.kube.wrapper.IntervalJS;
import com.github.llytho.lootjs.util.WeightedItemStack;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.minecraft.advancements.critereon.MinMaxBounds;

public class LootJSPlugin extends KubeJSPlugin {

    @Override
    public void initStartup() {
        LootModificationsAPI.DEBUG_ACTION = s -> ConsoleJS.SERVER.info(s);
    }

    @Override
    public void addBindings(BindingsEvent event) {
        event.add("LootType", LootContextType.class);
        event.add("Interval", new IntervalJS());
    }

    @Override
    public void addTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        typeWrappers.register(MinMaxBounds.Doubles.class, IntervalJS::ofDoubles);
        typeWrappers.register(MinMaxBounds.Ints.class, IntervalJS::ofInt);
        typeWrappers.register(WeightedItemStack.class, o -> {
            ItemStackJS itemStack = ItemStackJS.of(o);
            int weight = itemStack.hasChance() ? (int) itemStack.getChance() : 1;
            return new WeightedItemStack(itemStack.getItemStack(), weight);
        });
    }
}
