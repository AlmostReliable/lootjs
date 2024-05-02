package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.core.filters.ResourceLocationFilter;
import com.almostreliable.lootjs.loot.condition.builder.DistancePredicateBuilder;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface Predicates {

    static ItemPredicate.Builder item(Item... items) {
        return ItemPredicate.Builder.item().of(items);
    }

    static ItemPredicate.Builder itemTag(String tag) {
        return ItemPredicate.Builder.item().of(TagKey.create(Registries.ITEM, new ResourceLocation(tag)));
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
        return BlockPredicate.Builder.block().of(TagKey.create(Registries.BLOCK, new ResourceLocation(tag)));
    }

    static FluidPredicate.Builder fluid(Fluid fluid) {
        return FluidPredicate.Builder.fluid().of(fluid);
    }

    static FluidPredicate.Builder fluidTag(String tag) {
        TagKey<Fluid> tagKey = TagKey.create(Registries.FLUID, new ResourceLocation(tag));
        HolderSet.Named<Fluid> holderSet = BuiltInRegistries.FLUID.getOrCreateTag(tagKey);
        return FluidPredicate.Builder.fluid().of(holderSet);
    }

    static DamageSourcePredicate.Builder damageSource() {
        return DamageSourcePredicate.Builder.damageType();
    }

    static LightPredicate light(int min, int max) {
        return LightPredicate.Builder.light().setComposite(MinMaxBounds.Ints.between(min, max)).build();
    }

    static EnchantmentPredicate enchantment(Enchantment enchantment) {
        return new EnchantmentPredicate(enchantment, MinMaxBounds.Ints.ANY);
    }

    static EnchantmentPredicate enchantment(@Nullable Enchantment enchantment, MinMaxBounds.Ints levelBound) {
        return new EnchantmentPredicate(
                enchantment == null ? Optional.empty() : Optional.of(enchantment.builtInRegistryHolder()), levelBound);
    }

    static List<EnchantmentPredicate> enchantments(ResourceLocationFilter filter, MinMaxBounds.Ints levelBound) {
        List<EnchantmentPredicate> predicates = new ArrayList<>();
        if (filter instanceof ResourceLocationFilter.ByLocation byLoc) {
            var h = BuiltInRegistries.ENCHANTMENT.getHolderOrThrow(ResourceKey.create(Registries.ENCHANTMENT,
                    byLoc.location()));
            predicates.add(new EnchantmentPredicate(Optional.of(h), levelBound));
        } else {
            for (ResourceLocation id : BuiltInRegistries.ENCHANTMENT.keySet()) {
                if (!filter.test(id)) continue;
                var h = BuiltInRegistries.ENCHANTMENT.getHolderOrThrow(ResourceKey.create(Registries.ENCHANTMENT, id));
                predicates.add(new EnchantmentPredicate(Optional.of(h), levelBound));
            }
        }

        return predicates;
    }

    static ItemEnchantmentsPredicate itemEnchantments(ResourceLocationFilter filter, MinMaxBounds.Ints levelBound) {
        List<EnchantmentPredicate> predicates = enchantments(filter, levelBound);
        return ItemEnchantmentsPredicate.enchantments(predicates);
    }

    static ItemEnchantmentsPredicate storedEnchantments(ResourceLocationFilter filter, MinMaxBounds.Ints levelBound) {
        List<EnchantmentPredicate> predicates = enchantments(filter, levelBound);
        return ItemEnchantmentsPredicate.storedEnchantments(predicates);
    }

    static NbtPredicate nbt(CompoundTag nbt) {
        return new NbtPredicate(nbt);
    }


}
