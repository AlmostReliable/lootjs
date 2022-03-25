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
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.saveddata.maps.MapDecoration;

import javax.annotation.Nullable;

public class LootJSPlugin extends KubeJSPlugin {

    @Nullable
    public static <T extends Enum<T>> T valueOf(Class<T> clazz, Object o) {
        String s = o.toString();
        for (var constant : clazz.getEnumConstants()) {
            if (s.equalsIgnoreCase(constant.name())) {
                return constant;
            }
        }

        return null;
    }

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

        typeWrappers.register(MapDecoration.Type.class, o -> valueOf(MapDecoration.Type.class, o));
        typeWrappers.register(AttributeModifier.Operation.class, o -> valueOf(AttributeModifier.Operation.class, o));
    }
}
