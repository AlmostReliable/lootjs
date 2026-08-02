package com.almostreliable.lootjs.core.filters;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.predicates.DataComponentPredicate;
import net.minecraft.world.item.ItemStack;

public record ItemFilterWrapper(ItemFilter filter) implements DataComponentPredicate, ItemFilter {
    public static final Codec<ItemFilterWrapper> CODEC = MapCodec.unitCodec(() -> new ItemFilterWrapper(ItemFilter.NONE));
    public static final Type<ItemFilterWrapper> TYPE = new ConcreteType<>(ItemFilterWrapper.CODEC);

    @Override
    public boolean test(ItemStack itemStack) {
        return filter.test(itemStack);
    }

    @Override
    public boolean matches(DataComponentGetter dcg) {
        if (dcg instanceof ItemStack itemStack) {
            return filter.test(itemStack);
        }

        return false;
    }
}
