package com.almostreliable.lootjs.core.filters;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.world.item.ItemStack;

public record ItemFilterWrapper(ItemFilter filter) implements ItemSubPredicate, ItemFilter {
    public static final Codec<ItemFilterWrapper> CODEC = Codec.unit(() -> new ItemFilterWrapper(ItemFilter.NONE));
    public static final Type<ItemFilterWrapper> TYPE = new Type<>(ItemFilterWrapper.CODEC);

    @Override
    public boolean matches(ItemStack itemStack) {
        return filter.test(itemStack);
    }

    @Override
    public boolean test(ItemStack itemStack) {
        return filter.test(itemStack);
    }
}
