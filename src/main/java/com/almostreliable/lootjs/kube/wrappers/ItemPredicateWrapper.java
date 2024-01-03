package com.almostreliable.lootjs.kube.wrappers;

import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.advancements.critereon.ICustomItemPredicate;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ItemPredicateWrapper {

    private record ItemFilterPredicate(ItemFilter filter) implements ICustomItemPredicate {
        private static final ItemFilterPredicate INSTANCE = new ItemFilterPredicate(ItemFilter.ALWAYS_FALSE);
        private static final Codec<ItemFilterPredicate> CODEC = Codec.unit(INSTANCE);

        @Override
        public Codec<? extends ICustomItemPredicate> codec() {
            return CODEC;
        }

        @Override
        public boolean test(ItemStack itemStack) {
            return filter.test(itemStack);
        }
    }

    public static ItemPredicate.Builder ofBuilder(@Nullable Object o) {
        if (o instanceof ItemPredicate.Builder b) {
            return b;
        }

        return new SelfBuilder(of(o));
    }

    public static ItemPredicate of(@Nullable Object o) {
        if (o instanceof ItemPredicate i) return i;

        if (o instanceof ItemFilter filter) {
            new ItemPredicate(new ItemFilterPredicate(filter));
        }

        if (o instanceof String str && !str.isEmpty()) {
            String first = str.substring(0, 1);
            switch (first) {
                case "#":
                    var location = new ResourceLocation(str.substring(1));
                    var tag = TagKey.create(Registries.ITEM, location);
                    return new ItemPredicate(
                            Optional.of(tag),
                            Optional.empty(),
                            MinMaxBounds.Ints.ANY,
                            MinMaxBounds.Ints.ANY,
                            List.of(),
                            List.of(),
                            Optional.empty(),
                            Optional.empty()
                    );
                case "@":
                    String modId = str.substring(1);
                    ItemFilter filter = itemStack -> itemStack.kjs$getMod().equals(modId);
                    return new ItemPredicate(new ItemFilterPredicate(filter));
            }
        }

        var items = IngredientJS.of(o).getItems();
        Optional<HolderSet<Item>> holders = Optional.of(HolderSet.direct(ItemStack::getItemHolder, items));

        return new ItemPredicate(
                Optional.empty(),
                holders,
                MinMaxBounds.Ints.ANY,
                MinMaxBounds.Ints.ANY,
                List.of(),
                List.of(),
                Optional.empty(),
                Optional.empty()
        );
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
