package com.almostreliable.lootjs.kube;

import com.almostreliable.lootjs.LootEvents;
import com.almostreliable.lootjs.LootJS;
import com.almostreliable.lootjs.core.ItemStackFactory;
import com.almostreliable.lootjs.core.LootType;
import com.almostreliable.lootjs.core.entry.LootEntry;
import com.almostreliable.lootjs.core.entry.SingleLootEntry;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.core.filters.Resolver;
import com.almostreliable.lootjs.core.filters.ResourceLocationFilter;
import com.almostreliable.lootjs.kube.wrappers.ItemPredicateWrapper;
import com.almostreliable.lootjs.kube.wrappers.MinMaxBoundsWrapper;
import com.almostreliable.lootjs.kube.wrappers.MobEffectsPredicateWrapper;
import com.almostreliable.lootjs.kube.wrappers.StatePropsPredicateWrapper;
import com.almostreliable.lootjs.loot.LootCondition;
import com.almostreliable.lootjs.loot.LootFunction;
import com.almostreliable.lootjs.loot.Predicates;
import com.almostreliable.lootjs.loot.condition.builder.DistancePredicateBuilder;
import com.almostreliable.lootjs.util.BlockFilter;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.mod.util.NBTUtils;
import dev.latvian.mods.rhino.util.wrap.TypeWrapperFactory;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.holdersets.OrHolderSet;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

public class LootJSPlugin extends KubeJSPlugin {

    public static final EntityTypePredicate EMPTY_ENTITY_TYPE_PREDICATE = new EntityTypePredicate(HolderSet.direct());

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

    private static ItemFilter ofItemFilterSingle(@Nullable Object o) {
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
                    return itemStack -> {
                        var key = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
                        return key.getNamespace().equals(modId);
                    };
            }
        }

        Ingredient ingredient = IngredientJS.of(o);
        if (ingredient.isEmpty()) {
            return ItemFilter.EMPTY;
        }

        return new ItemFilter.Ingredient(ingredient);
    }

    private static ItemFilter ofItemFilter(Object o) {
        if (o instanceof List<?> list) {
            List<ItemFilter> filters = new ArrayList<>(list.size());
            for (Object entry : list) {
                var filter = ofItemFilter(entry);
                filters.add(filter);
            }

            return itemStack -> {
                for (ItemFilter filter : filters) {
                    if (filter.test(itemStack)) {
                        return true;
                    }
                }

                return false;
            };
        }

        return ofItemFilterSingle(o);
    }

    private static BlockFilter ofBlockFilter(Object o) {
        if (o instanceof BlockFilter bf) {
            return bf;
        }

        BlockStatePredicate bsp = BlockStatePredicate.of(o);
        return () -> bsp.getBlocks().iterator();
    }

    @Override
    public void initStartup() {
        LootJS.DEBUG_ACTION = s -> ConsoleJS.SERVER.info(s);
        LootEvents.listen(registry -> {
            var event = new LootTableEventJS(registry);
            LootJSEvent.LOOT_TABLES.post(event);
        });

        LootEvents.listenModifiers(modifiers -> {
            var event = new LootModificationEventJS(modifiers);
            LootJSEvent.MODIFIERS.post(event);
        });
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

        event.add("IntBounds", MinMaxBounds.Ints.class);
        event.add("Bounds", MinMaxBounds.Doubles.class);

        event.add("Predicates", Predicates.class);
        event.add("ItemPredicate", ItemPredicate.class);
        event.add("EntityPredicate", EntityPredicate.class);
        event.add("EntityEquipmentPredicate", EntityEquipmentPredicate.class);
        event.add("LocationPredicate", LocationPredicate.class);
        event.add("DistancePredicate", DistancePredicate.class);
        event.add("BlockPredicate", BlockPredicate.class);
        event.add("FluidPredicate", FluidPredicate.class);
        event.add("LightPredicate", LightPredicate.class);
        event.add("EnchantmentPredicate", EnchantmentPredicate.class);
        event.add("MobEffectsPredicate", MobEffectsPredicate.class);
        event.add("NbtPredicate", NbtPredicate.class);
    }

    @Override
    public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        typeWrappers.registerSimple(BlockFilter.class, LootJSPlugin::ofBlockFilter);
        typeWrappers.registerSimple(LootEntry.class, LootJSPlugin::ofLootEntry);
        typeWrappers.registerSimple(SingleLootEntry.class, LootJSPlugin::ofSingleLootEntry);
        typeWrappers.registerSimple(ItemStackFactory.class, LootJSPlugin::ofItemStackFactory);
        typeWrappers.registerSimple(MinMaxBounds.Doubles.class, MinMaxBoundsWrapper::ofMinMaxDoubles);
        typeWrappers.registerSimple(MinMaxBounds.Ints.class, MinMaxBoundsWrapper::ofMinMaxInt);
        typeWrappers.registerSimple(EntityTypePredicate.class, LootJSPlugin::ofEntityTypePredicate);
        typeWrappers.registerSimple(NbtPredicate.class, LootJSPlugin::ofNbtPredicate);
        typeWrappers.registerSimple(StatePropertiesPredicate.class, StatePropsPredicateWrapper::of);
        typeWrappers.registerSimple(StatePropertiesPredicate.Builder.class, StatePropsPredicateWrapper::ofBuilder);
        typeWrappers.registerSimple(MobEffectsPredicate.class, MobEffectsPredicateWrapper::of);
        typeWrappers.registerSimple(MobEffectsPredicate.Builder.class, MobEffectsPredicateWrapper::ofBuilder);
        typeWrappers.registerSimple(DistancePredicate.class, LootJSPlugin::ofDistancePredicate);

        typeWrappers.registerSimple(ItemPredicate.Builder.class, ItemPredicateWrapper::ofBuilder);
        typeWrappers.registerSimple(ItemPredicate.class, ItemPredicateWrapper::of);
//        createBuilderWrapper(typeWrappers,
//                EntityPredicate.class,
//                EntityPredicate.Builder.class,
//                EntityPredicate.Builder::build);
//        createBuilderWrapper(typeWrappers,
//                ItemPredicate.class,
//                ItemPredicate.Builder.class,
//                ItemPredicate.Builder::build);
//        createBuilderWrapper(typeWrappers,
//                EntityEquipmentPredicate.class,
//                EntityEquipmentPredicate.Builder.class,
//                EntityEquipmentPredicate.Builder::build);
//        createBuilderWrapper(typeWrappers,
//                LocationPredicate.class,
//                LocationPredicate.Builder.class,
//                LocationPredicate.Builder::build);
//        createBuilderWrapper(typeWrappers,
//                DistancePredicate.class,
//                DistancePredicateBuilder.class,
//                DistancePredicateBuilder::build);
//        createBuilderWrapper(typeWrappers,
//                BlockPredicate.class,
//                BlockPredicate.Builder.class,
//                BlockPredicate.Builder::build);
//        createBuilderWrapper(typeWrappers,
//                FluidPredicate.class,
//                FluidPredicate.Builder.class,
//                FluidPredicate.Builder::build);
//        createBuilderWrapper(typeWrappers,
//                DamageSourcePredicate.class,
//                DamageSourcePredicate.Builder.class,
//                DamageSourcePredicate.Builder::build);

        typeWrappers.registerSimple(ItemFilter.class, LootJSPlugin::ofItemFilter);

        typeWrappers.registerSimple(ResourceLocationFilter.class, this::ofResourceLocationFilter);
//        typeWrappers.registerSimple(MapDecoration.Type.class, o -> valueOf(MapDecoration.Type.class, o)); // TODO Add back
        typeWrappers.registerSimple(Resolver.class, o -> Resolver.of(o.toString()));
    }

    private static DistancePredicate ofDistancePredicate(Object o) {
        if (o instanceof DistancePredicate distancePredicate) {
            return distancePredicate;
        }

        if (o instanceof DistancePredicateBuilder distancePredicateBuilder) {
            return distancePredicateBuilder.build();
        }

        LootJS.LOG.warn("Invalid distance predicate: {}", o);
        return DistancePredicate.absolute(MinMaxBounds.Doubles.exactly(Double.MAX_VALUE));
    }

    private static NbtPredicate ofNbtPredicate(@Nullable Object o) {
        if (o instanceof NbtPredicate nbt) {
            return nbt;
        }

        if (o instanceof Map<?, ?> map) {
            return new NbtPredicate((CompoundTag) NBTUtils.compoundTag(map));
        }

        return new NbtPredicate(new CompoundTag());
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

        ItemStack itemStack = ItemStackJS.of(o);
        if (itemStack.isEmpty()) {
            ConsoleJS.SERVER.error("[LootEntry.of()] Invalid item stack, returning empty stack: " + o);
            ConsoleJS.SERVER.error("- Consider using `LootEntry.empty()` if you want to create an empty loot entry.");
            return LootEntry.empty();
        }

        return LootEntry.of(itemStack);
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

    public static EntityTypePredicate ofEntityTypePredicate(@Nullable Object o) {
        if (o instanceof EntityType<?> type) {
            return EntityTypePredicate.of(type);
        }

        if (o instanceof TagKey<?> tag) {
            if (tag.registry() != Registries.ENTITY_TYPE) {
                throw new IllegalArgumentException("Provided tag is not an entity type tag: " + tag);
            }

            //noinspection unchecked
            return EntityTypePredicate.of((TagKey<EntityType<?>>) tag);
        }

        if (o instanceof String str) {
            if (str.startsWith("#")) {
                ResourceLocation tag = new ResourceLocation(str.substring(1));
                return EntityTypePredicate.of(TagKey.create(Registries.ENTITY_TYPE, tag));
            }

            EntityType<?> et = BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(str));
            return EntityTypePredicate.of(et);
        }

        if (o instanceof List<?> list) {
            List<HolderSet<EntityType<?>>> predicates = new ArrayList<>();
            for (Object object : list) {
                var p = ofEntityTypePredicate(object);
                predicates.add(p.types());
            }

            return new EntityTypePredicate(new OrHolderSet<>(predicates));
        }

        LootJS.LOG.error("Failed creating EntityTypePredicate. Will return empty one");
        return EMPTY_ENTITY_TYPE_PREDICATE;
    }

    private static <T, B> void createBuilderWrapper(TypeWrappers wrappers, Class<T> goalClazz, Class<B> builderClazz, Function<B, T> buildFunc) {
        wrappers.registerSimple(goalClazz, new BuilderTypeWrapper<>(goalClazz, builderClazz, buildFunc));
    }

    private record BuilderTypeWrapper<T, B>(Class<T> goalClazz, Class<B> builderClazz, Function<B, T> buildFunc)
            implements TypeWrapperFactory.Simple<T> {

        @Override
        public T wrapSimple(Object o) {
            if (goalClazz.isInstance(o)) {
                return goalClazz().cast(o);
            }

            if (builderClazz.isInstance(o)) {
                return buildFunc().apply(builderClazz.cast(o));
            }

            throw new IllegalArgumentException(
                    "LootJS Type Error. Could not build or direct cast into " + goalClazz.getSimpleName() + ", got: " +
                    o);
        }
    }
}
