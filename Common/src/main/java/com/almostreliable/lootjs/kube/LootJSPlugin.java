package com.almostreliable.lootjs.kube;

import com.almostreliable.lootjs.LootJSPlatform;
import com.almostreliable.lootjs.LootModificationsAPI;
import com.almostreliable.lootjs.core.ItemStackFactory;
import com.almostreliable.lootjs.core.LootType;
import com.almostreliable.lootjs.core.entry.LootEntry;
import com.almostreliable.lootjs.core.entry.SingleLootEntry;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.core.filters.Resolver;
import com.almostreliable.lootjs.core.filters.ResourceLocationFilter;
import com.almostreliable.lootjs.loot.LootCondition;
import com.almostreliable.lootjs.loot.LootFunction;
import com.almostreliable.lootjs.util.EntityTypeFilter;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.saveddata.maps.MapDecoration;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

        if (o instanceof String str && !str.isEmpty()) {
            String first = str.substring(0, 1);
            switch (first) {
                case "*":
                    return ItemFilter.ALWAYS_TRUE;
                case "#":
                    ResourceLocation location = new ResourceLocation(str.substring(1));
                    TagKey<Item> tag = TagKey.create(Registries.ITEM, location);
                    return new ItemFilter.Tag(tag);
                case "@":
                    String modId = str.substring(1);
                    return itemStack -> itemStack.kjs$getMod().equals(modId);
            }
        }

        Ingredient ingredient = IngredientJS.of(o);
        if (ingredient.isEmpty()) {
            return ItemFilter.EMPTY;
        }

        return new ItemFilter.Ingredient(ingredient);
    }

    @Override
    public void initStartup() {
        LootModificationsAPI.DEBUG_ACTION = s -> ConsoleJS.SERVER.info(s);
    }

    @Override
    public void registerEvents() {
        LootJSEvent.GROUP.register();
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("LootType", LootType.class);
        event.add("ItemFilter", ItemFilter.class);
        event.add("LootEntry", LootEntry.class);
        event.add("LootCondition", LootCondition.class);
        event.add("LootFunction", LootFunction.class);
        LootJSPlatform.INSTANCE.registerBindings(event);
    }

    @Override
    public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        typeWrappers.registerSimple(LootEntry.class, LootJSPlugin::ofLootEntry);
        typeWrappers.registerSimple(SingleLootEntry.class, LootJSPlugin::ofSingleLootEntry);
        typeWrappers.registerSimple(ItemStackFactory.class, LootJSPlugin::ofItemStackFactory);
        typeWrappers.registerSimple(MinMaxBounds.Doubles.class, LootJSPlugin::ofMinMaxDoubles);
        typeWrappers.registerSimple(MinMaxBounds.Ints.class, LootJSPlugin::ofMinMaxInt);
        typeWrappers.registerSimple(EntityTypeFilter.class, EntityTypeFilter::of);

        typeWrappers.registerSimple(ItemFilter.class, o -> {
            if (o instanceof List<?> list) {
                Map<Boolean, ? extends List<?>> split = list
                        .stream()
                        .collect(Collectors.partitioningBy(unknown -> unknown instanceof ItemFilter));
                List<ItemFilter> itemFilters = new ArrayList<>(split
                        .get(true)
                        .stream()
                        .map(LootJSPlugin::ofItemFilter)
                        .toList());
                if (!split.get(false).isEmpty()) {
                    Ingredient ingredientFilter = IngredientJS.of(split.get(false));
                    itemFilters.add(ItemFilter.custom(ingredientFilter));
                }
                return ItemFilter.or(itemFilters.toArray(ItemFilter[]::new));
            }

            return ofItemFilter(o);
        });

        typeWrappers.registerSimple(ResourceLocationFilter.class, this::ofResourceLocationFilter);
        typeWrappers.registerSimple(MapDecoration.Type.class, o -> valueOf(MapDecoration.Type.class, o));
        typeWrappers.registerSimple(AttributeModifier.Operation.class, o -> valueOf(AttributeModifier.Operation.class, o));
        typeWrappers.registerSimple(Resolver.class, o -> Resolver.of(o.toString()));
    }

    private ResourceLocationFilter ofResourceLocationFilter(Object o) {
        if (o instanceof List<?> list) {
            return new ResourceLocationFilter.Or(list.stream().map(this::ofResourceLocationFilter).toList());
        }

        Pattern pattern = UtilsJS.parseRegex(o);
        if (pattern == null) {
            return new ResourceLocationFilter.ByLocation(new ResourceLocation(o.toString()));
        } else {
            return new ResourceLocationFilter.ByPattern(pattern);
        }
    }

    private static ItemStackFactory ofItemStackFactory(@Nullable Object o) {
        if (o instanceof ItemStackFactory factory) {
            return factory;
        }

        ItemStack itemStack = ItemStackJS.of(o);
        if (itemStack.isEmpty()) {
            return ItemStackFactory.EMPTY;
        }

        return context -> itemStack;
    }

    public static SingleLootEntry ofSingleLootEntry(@Nullable Object o) {
        if (o instanceof SingleLootEntry e) {
            return e;
        }

        OutputItem outputItem = OutputItem.of(o);
        ItemStack itemStack = outputItem.item;
        if (itemStack.isEmpty()) {
            ConsoleJS.SERVER.error("[LootEntry.of()] Invalid item stack, returning empty stack: " + o);
            ConsoleJS.SERVER.error("- Consider using `LootEntry.empty()` if you want to create an empty loot entry.");
            return LootEntry.empty();
        }

        var entry = LootEntry.of(itemStack);
        if (outputItem.hasChance()) {
            entry.setWeight((int) outputItem.getChance());
        }

        return entry;
    }

    public static LootEntry ofLootEntry(@Nullable Object o) {
        if (o instanceof LootEntry entry) {
            return entry;
        }

        if (o instanceof String str && str.startsWith("#")) {
            String tag = str.substring(0, 1);
            return LootEntry.tag(tag, false);
        }

        return ofSingleLootEntry(o);
    }

    public static MinMaxBounds.Doubles ofMinMaxDoubles(Object o) {
        if (o instanceof List<?> list) {
            if (list.size() == 1) {
                return ofMinMaxDoubles(list.get(0));
            }

            if (list.size() == 2) {
                Object min = list.get(0);
                Object max = list.get(1);
                if (min instanceof Number minN && max instanceof Number maxN) {
                    return MinMaxBounds.Doubles.between(minN.doubleValue(), maxN.doubleValue());
                }
            }
        }

        if (o instanceof Number) {
            return MinMaxBounds.Doubles.atLeast(((Number) o).doubleValue());
        }

        if (o instanceof MinMaxBounds<? extends Number> minMaxBounds) {
            Double min = minMaxBounds.getMin() != null ? minMaxBounds.getMin().doubleValue() : null;
            Double max = minMaxBounds.getMax() != null ? minMaxBounds.getMax().doubleValue() : null;
            return new MinMaxBounds.Doubles(min, max);
        }

        throw new IllegalArgumentException("Argument is not a MinMaxBound");
    }

    public static MinMaxBounds.Ints ofMinMaxInt(Object o) {
        if (o instanceof MinMaxBounds.Ints) {
            return (MinMaxBounds.Ints) o;
        }

        MinMaxBounds.Doubles doubles = ofMinMaxDoubles(o);
        Integer min = doubles.getMin() != null ? doubles.getMin().intValue() : null;
        Integer max = doubles.getMax() != null ? doubles.getMax().intValue() : null;
        return new MinMaxBounds.Ints(min, max);
    }
}
