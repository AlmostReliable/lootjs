package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.core.filters.IdFilter;
import com.almostreliable.lootjs.loot.condition.builder.DistancePredicateBuilder;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface Predicates {

    public static HolderLookup.Provider lookup() {
        // TODO fix
        return null;
    }

    static ItemPredicate.Builder item(Item... items) {
        return ItemPredicate.Builder.item().of(items);
    }

    static ItemPredicate.Builder itemTag(String tag) {
        return ItemPredicate.Builder.item().of(TagKey.create(Registries.ITEM, ResourceLocation.parse(tag)));
    }

    static EntityPredicate.Builder entity(EntityTypePredicate predicates) {
        return EntityPredicate.Builder.entity().entityType(predicates);
    }

    static EntityPredicate.Builder entity() {
        return EntityPredicate.Builder.entity();
    }

    static EntityEquipmentPredicate.Builder equipment() {
        return EntityEquipmentPredicate.Builder.equipment();
    }

    static LocationPredicate.Builder location() {
        return LocationPredicate.Builder.location();
    }

    static DistancePredicateBuilder distance() {
        return new DistancePredicateBuilder();
    }

    static BlockPredicate.Builder block(Block... blocks) {
        return BlockPredicate.Builder.block().of(blocks);
    }

    static BlockPredicate.Builder blockTag(String tag) {
        return BlockPredicate.Builder.block().of(TagKey.create(Registries.BLOCK, ResourceLocation.parse(tag)));
    }

    static FluidPredicate.Builder fluid(Fluid fluid) {
        return FluidPredicate.Builder.fluid().of(fluid);
    }

    static FluidPredicate.Builder fluidTag(String tag) {
        TagKey<Fluid> tagKey = TagKey.create(Registries.FLUID, ResourceLocation.parse(tag));
        HolderSet.Named<Fluid> holderSet = BuiltInRegistries.FLUID.getOrCreateTag(tagKey);
        return FluidPredicate.Builder.fluid().of(holderSet);
    }

    static DamageSourcePredicate.Builder damageSource() {
        return DamageSourcePredicate.Builder.damageType();
    }

    static LightPredicate light(int min, int max) {
        return LightPredicate.Builder.light().setComposite(MinMaxBounds.Ints.between(min, max)).build();
    }

    static EnchantmentPredicate enchantment(IdFilter filter) {
        return enchantment(filter, MinMaxBounds.Ints.ANY);
    }

    static EnchantmentPredicate enchantment(IdFilter filter, MinMaxBounds.Ints levelBound) {
        List<Holder.Reference<Enchantment>> enchantments = lookup()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .listElements()
                .filter(ref -> filter.test(ref.key().location()))
                .toList();

        HolderSet.Direct<Enchantment> holderSet = HolderSet.direct(enchantments);
        return new EnchantmentPredicate(Optional.of(holderSet), levelBound);
    }

    static ItemEnchantmentsPredicate itemEnchantments(EnchantmentPredicate[] predicates) {
        return ItemEnchantmentsPredicate.enchantments(Arrays.asList(predicates));
    }

    static ItemEnchantmentsPredicate storedEnchantments(EnchantmentPredicate[] predicates) {
        return ItemEnchantmentsPredicate.storedEnchantments(Arrays.asList(predicates));
    }

    static NbtPredicate nbt(CompoundTag nbt) {
        return new NbtPredicate(nbt);
    }


}
