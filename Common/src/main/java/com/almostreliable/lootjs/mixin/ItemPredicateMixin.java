package com.almostreliable.lootjs.mixin;

import com.almostreliable.lootjs.core.filters.ItemFilter;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemPredicate.class)
public abstract class ItemPredicateMixin implements ItemFilter {
    @Shadow
    public abstract boolean matches(ItemStack item);

    @Override
    public boolean test(ItemStack itemStack) {
        return this.matches(itemStack);
    }
}
