package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.core.filters.IdFilter;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.NbtPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface Predicates {

    public static HolderLookup.Provider lookup() {
        // TODO fix
        return null;
    }

    static EnchantmentPredicate enchantment(IdFilter filter) {
        return enchantment(filter, MinMaxBounds.Ints.ANY);
    }

    static EnchantmentPredicate enchantment(IdFilter filter, MinMaxBounds.Ints levelBound) {
        List<Holder.Reference<Enchantment>> enchantments = lookup()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .listElements().filter(ref -> filter.test(ref.key().identifier()))
                .toList();

        HolderSet.Direct<Enchantment> holderSet = HolderSet.direct(enchantments);
        return new EnchantmentPredicate(Optional.of(holderSet), levelBound);
    }

    static EnchantmentsPredicate itemEnchantments(EnchantmentPredicate[] predicates) {
        return EnchantmentsPredicate.enchantments(Arrays.asList(predicates));
    }

    static EnchantmentsPredicate storedEnchantments(EnchantmentPredicate[] predicates) {
        return EnchantmentsPredicate.storedEnchantments(Arrays.asList(predicates));
    }

    static NbtPredicate nbt(CompoundTag nbt) {
        return new NbtPredicate(nbt);
    }


}
