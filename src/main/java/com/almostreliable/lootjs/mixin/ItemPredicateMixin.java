package com.almostreliable.lootjs.mixin;

import com.almostreliable.lootjs.core.filters.ItemFilter;
import net.minecraft.advancements.criterion.ItemPredicate;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemPredicate.class)
public abstract class ItemPredicateMixin implements ItemFilter {
}
