package com.almostreliable.lootjs.kube.wrappers;

import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.core.filters.ItemFilterWrapper;
import com.almostreliable.lootjs.kube.KubeOps;
import dev.latvian.mods.kubejs.script.KubeJSContext;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ItemPredicateWrapper {

    private static final TypeInfo ITEM_HOLDER_SET = TypeInfo.of(HolderSet.class).withParams(TypeInfo.of(Item.class));

    @SuppressWarnings("unchecked")
    public static ItemPredicate of(Context cx, Object o, TypeInfo target) {
        return switch (o) {
            case ItemPredicate i -> i;
            case ItemFilter filter -> ItemPredicate.Builder
                    .item()
                    .withSubPredicate(ItemFilterWrapper.TYPE, new ItemFilterWrapper(filter))
                    .build();
            case Map<?, ?> map -> {
                RegistryAccessContainer registries = ((KubeJSContext) cx).getRegistries();
                KubeOps ops = KubeOps.create(registries);
                yield ItemPredicate.CODEC.parse(ops, o).getOrThrow();
            }
            default -> {
                var set = (HolderSet<Item>) cx.jsToJava(o, ITEM_HOLDER_SET);
                yield new ItemPredicate(Optional.of(set),
                        MinMaxBounds.Ints.ANY,
                        DataComponentPredicate.EMPTY,
                        new HashMap<>());
            }
        };
    }
}
