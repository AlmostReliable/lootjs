package com.almostreliable.lootjs.kube.wrappers;

import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.core.filters.ItemFilterWrapper;
import com.almostreliable.lootjs.util.ModHolderSet;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.rhino.Context;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.holdersets.AnyHolderSet;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Optional;

public class ItemPredicateWrapper {

    public static ItemPredicate.Builder ofBuilder(Context cx, @Nullable Object o) {
        if (o instanceof ItemPredicate.Builder b) {
            return b;
        }

        return new SelfBuilder(of(cx, o));
    }

    public static ItemPredicate of(Context cx, @Nullable Object o) {
        if (o instanceof ItemPredicate i) return i;

        if (o instanceof ItemFilter filter) {
            return ItemPredicate.Builder
                    .item()
                    .withSubPredicate(ItemFilterWrapper.TYPE, new ItemFilterWrapper(filter))
                    .build();
        }

        if (o instanceof String str && !str.isEmpty()) {
            String first = str.substring(0, 1);
            switch (first) {
                case "*" -> {
                    var anyHolder = new AnyHolderSet<>(BuiltInRegistries.ITEM.asLookup());
                    return new ItemPredicate(Optional.of(anyHolder),
                            MinMaxBounds.Ints.ANY,
                            DataComponentPredicate.EMPTY,
                            new HashMap<>());
                }
                case "#" -> {
                    var location = ResourceLocation.parse(str.substring(1));
                    var tag = TagKey.create(Registries.ITEM, location);
                    var holderSet = BuiltInRegistries.ITEM.getOrCreateTag(tag);
                    return new ItemPredicate(Optional.of(holderSet),
                            MinMaxBounds.Ints.ANY,
                            DataComponentPredicate.EMPTY,
                            new HashMap<>());
                }
                case "@" -> {
                    String modId = str.substring(1);
                    ModHolderSet<Item> holderSet = new ModHolderSet<>(BuiltInRegistries.ITEM.asLookup(), modId);
                    return new ItemPredicate(Optional.of(holderSet),
                            MinMaxBounds.Ints.ANY,
                            DataComponentPredicate.EMPTY,
                            new HashMap<>());
                }
            }
        }

        var items = IngredientJS.wrap(cx, o).getItems();
        Optional<HolderSet<Item>> holders = Optional.of(HolderSet.direct(ItemStack::getItemHolder, items));

        return new ItemPredicate(holders,
                MinMaxBounds.Ints.ANY,
                DataComponentPredicate.EMPTY,
                new HashMap<>());
    }

    private static class SelfBuilder extends ItemPredicate.Builder {
        private final ItemPredicate predicate;

        public SelfBuilder(ItemPredicate predicate) {
            this.predicate = predicate;
        }

        @Override
        public ItemPredicate build() {
            return predicate;
        }
    }
}
