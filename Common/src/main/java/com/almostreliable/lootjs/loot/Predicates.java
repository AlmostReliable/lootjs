package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.loot.condition.builder.DistancePredicateBuilder;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nullable;

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
        return FluidPredicate.Builder.fluid().of(TagKey.create(Registries.FLUID, new ResourceLocation(tag)));
    }

    static LightPredicate light(int min, int max) {
        return LightPredicate.Builder.light().setComposite(MinMaxBounds.Ints.between(min, max)).build();
    }

    static EnchantmentPredicate enchantment(@Nullable Enchantment enchantment, MinMaxBounds.Ints levels) {
        return new EnchantmentPredicate(enchantment, levels);
    }

    static MobEffectsPredicate mobEffects(MobEffect effect) {
        return MobEffectsPredicate.effects();
    }

    static MobEffectsPredicate mobEffects(MobEffect effect, MobEffectsPredicate.MobEffectInstancePredicate predicate) {
        return MobEffectsPredicate.effects().and(effect, predicate);
    }

    static NbtPredicate nbt(CompoundTag nbt) {
        return new NbtPredicate(nbt);
    }


}
