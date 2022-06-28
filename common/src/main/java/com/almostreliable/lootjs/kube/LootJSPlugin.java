package com.almostreliable.lootjs.kube;

import com.almostreliable.lootjs.LootModificationsAPI;
import com.almostreliable.lootjs.core.LootContextType;
import com.almostreliable.lootjs.core.LootEntry;
import com.almostreliable.lootjs.filters.ItemFilter;
import com.almostreliable.lootjs.filters.Resolver;
import com.almostreliable.lootjs.filters.ResourceLocationFilter;
import com.almostreliable.lootjs.kube.wrapper.IntervalJS;
import com.almostreliable.lootjs.util.WeightedItemStack;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapDecoration;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class LootJSPlugin extends KubeJSPlugin {

    public static boolean eventsAreDisabled() {
        return Boolean.getBoolean("lootjs.disable_events");
    }

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

    private static ItemFilter ofItemFilter(@Nullable Object o) {
        if (o instanceof ItemFilter i) return i;

        IngredientJS ijs = IngredientJS.of(o);
        if (ijs.isEmpty()) {
            return ItemFilter.ALWAYS_TRUE;
        }

        Predicate<ItemStack> vanillaPredicate = ijs.getVanillaPredicate();
        return vanillaPredicate::test;
    }

    @Override
    public void initStartup() {
        LootModificationsAPI.DEBUG_ACTION = s -> ConsoleJS.SERVER.info(s);
    }

    @Override
    public void addBindings(BindingsEvent event) {
        event.add("LootType", LootContextType.class);
        event.add("Interval", new IntervalJS());
        event.add("ItemFilter", ItemFilter.class);
        event.add("LootEntry", LootEntryWrapper.class);
    }

    @Override
    public void addTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        typeWrappers.register(LootEntry.class, LootEntryWrapper::of);
        typeWrappers.register(MinMaxBounds.Doubles.class, IntervalJS::ofDoubles);
        typeWrappers.register(MinMaxBounds.Ints.class, IntervalJS::ofInt);

        typeWrappers.register(WeightedItemStack.class, o -> {
            ItemStackJS itemStack = ItemStackJS.of(o);
            int weight = itemStack.hasChance() ? (int) itemStack.getChance() : 1;
            return new WeightedItemStack(itemStack.getItemStack(), weight);
        });

        typeWrappers.register(ItemFilter.class, o -> {
            if (o instanceof List<?> list) {
                return ItemFilter.and(list.stream().map(LootJSPlugin::ofItemFilter).toArray(ItemFilter[]::new));
            }

            return ofItemFilter(o);
        });

        typeWrappers.register(ResourceLocationFilter.class, o -> {
            Pattern pattern = UtilsJS.parseRegex(o);
            if (pattern == null) {
                return new ResourceLocationFilter.Equals(new ResourceLocation(o.toString()));
            } else {
                return new ResourceLocationFilter.ByPattern(pattern);
            }
        });

        typeWrappers.register(MapDecoration.Type.class, o -> valueOf(MapDecoration.Type.class, o));
        typeWrappers.register(AttributeModifier.Operation.class, o -> valueOf(AttributeModifier.Operation.class, o));
        typeWrappers.register(Resolver.class, o -> Resolver.of(o.toString()));
    }
}
