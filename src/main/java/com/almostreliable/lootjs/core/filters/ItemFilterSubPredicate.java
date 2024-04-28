package com.almostreliable.lootjs.core.filters;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.world.item.ItemStack;

public record ItemFilterSubPredicate(ItemFilter filter) implements ItemSubPredicate, ItemFilter {
    public static final Codec<ItemFilterSubPredicate> CODEC = Codec.unit(() -> new ItemFilterSubPredicate(ItemFilter.ALWAYS_FALSE));
    public static final Type<ItemFilterSubPredicate> TYPE = new Type<>(ItemFilterSubPredicate.CODEC);

    @Override
    public boolean matches(ItemStack itemStack) {
        return filter.test(itemStack);
    }

    @Override
    public boolean test(ItemStack itemStack) {
        return filter.test(itemStack);
    }
}
