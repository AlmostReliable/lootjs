package com.almostreliable.lootjs.kube.wrappers;

import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.core.filters.ItemFilterWrapper;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.type.RecordTypeInfo;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class ItemPredicateWrapper {

    private static final TypeInfo ITEM_HOLDER_SET = TypeInfo.of(HolderSet.class).withParams(TypeInfo.of(Item.class));

    public static ItemPredicate of(Context cx, @Nullable Object o, TypeInfo target) {
        if (o instanceof ItemPredicate i) return i;

        if (o instanceof ItemFilter filter) {
            return ItemPredicate.Builder
                    .item()
                    .withSubPredicate(ItemFilterWrapper.TYPE, new ItemFilterWrapper(filter))
                    .build();
        }

        if (o instanceof String | o instanceof Pattern) {
            @SuppressWarnings("unchecked")
            var set = (HolderSet<Item>) cx.jsToJava(o, ITEM_HOLDER_SET);
            return new ItemPredicate(Optional.of(set),
                    MinMaxBounds.Ints.ANY,
                    DataComponentPredicate.EMPTY,
                    new HashMap<>());
        }

        return (ItemPredicate) ((RecordTypeInfo) target).wrap(cx, o, target);
    }
}
