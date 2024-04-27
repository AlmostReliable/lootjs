package com.almostreliable.lootjs.kube.wrappers;

import com.almostreliable.lootjs.core.filters.ItemFilter;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
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

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Optional;

public class ItemPredicateWrapper {
//
//    private record ItemFilterPredicate(ItemFilter filter) implements ICustomItemPredicate {
//        private static final ItemFilterPredicate INSTANCE = new ItemFilterPredicate(ItemFilter.ALWAYS_FALSE);
//        private static final Codec<ItemFilterPredicate> CODEC = Codec.unit(INSTANCE);
//
//        @Override
//        public Codec<? extends ICustomItemPredicate> codec() {
//            return CODEC;
//        }
//
//        @Override
//        public boolean test(ItemStack itemStack) {
//            return filter.test(itemStack);
//        }
//    }

    public static ItemPredicate.Builder ofBuilder(@Nullable Object o) {
        if (o instanceof ItemPredicate.Builder b) {
            return b;
        }

        return new SelfBuilder(of(o));
    }

    public static ItemPredicate of(@Nullable Object o) {
        if (o instanceof ItemPredicate i) return i;

        if (o instanceof ItemFilter filter) {
//            new ItemPredicate(new ItemFilterPredicate(filter));
            throw new UnsupportedOperationException("TODO");
        }

        if (o instanceof String str && !str.isEmpty()) {
            String first = str.substring(0, 1);
            if (first.equals("#")) {
                var location = new ResourceLocation(str.substring(1));
                var tag = TagKey.create(Registries.ITEM, location);
                var holderSet = BuiltInRegistries.ITEM.getOrCreateTag(tag);
                return new ItemPredicate(Optional.of(holderSet),
                        MinMaxBounds.Ints.ANY,
                        DataComponentPredicate.EMPTY,
                        new HashMap<>());
//                case "@": // TODO create custom holder set for this?
//                    String modId = str.substring(1);
//                    ItemFilter filter = itemStack -> itemStack.kjs$getMod().equals(modId);
//                    return new ItemPredicate(new ItemFilterPredicate(filter));
            }
        }

        var items = IngredientJS.of(o).getItems();
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
