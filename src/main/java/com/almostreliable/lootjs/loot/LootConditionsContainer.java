package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.core.filters.Resolver;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings({ "UnusedReturnValue", "unused" })
public interface LootConditionsContainer<B extends LootConditionsContainer<?>> {

    default B matchTool(ItemPredicate predicate) {
        return addCondition(LootCondition.matchTool(predicate));
    }

    default B matchMainHand(ItemFilter filter) {
        return addCondition(LootCondition.matchMainHand(filter));
    }

    default B matchOffHand(ItemFilter filter) {
        return addCondition(LootCondition.matchOffHand(filter));
    }

    default B matchEquip(EquipmentSlot slot, ItemFilter filter) {
        return addCondition(LootCondition.matchEquip(slot, filter));
    }

    default B survivesExplosion() {
        return addCondition(LootCondition.survivesExplosion());
    }

    default B timeCheck(long period, int min, int max) {
        return addCondition(LootCondition.timeCheck(period, min, max));
    }

    default B timeCheck(int min, int max) {
        return addCondition(LootCondition.timeCheck(min, max));
    }

    default B weatherCheck(Map<String, Boolean> map) {
        return addCondition(LootCondition.weatherCheck(map));
    }

    default B weatherCheck(Boolean isRaining, Boolean isThundering) {
        return addCondition(LootCondition.weatherCheck(isRaining, isThundering));
    }

    default B randomChance(float value) {
        return addCondition(LootCondition.randomChance(value));
    }

    default B randomChanceWithLooting(float value, float looting) {
        return addCondition(LootCondition.randomChanceWithLooting(value, looting));
    }

    default B randomChanceWithEnchantment(@Nullable Enchantment enchantment, float[] chances) {
        return addCondition(LootCondition.randomChanceWithEnchantment(enchantment, chances));
    }

    default B location(LocationPredicate.Builder predicate) {
        return addCondition(LootCondition.location(predicate));
    }

    default B location(BlockPos offset, LocationPredicate.Builder predicate) {
        return addCondition(LootCondition.location(offset, predicate));
    }

    default B biome(Resolver... resolvers) {
        return addCondition(LootCondition.biome(resolvers));
    }

    default B anyBiome(Resolver... resolvers) {
        return addCondition(LootCondition.anyBiome(resolvers));
    }

    default B anyDimension(ResourceLocation... dimensions) {
        return addCondition(LootCondition.anyDimension(dimensions));
    }

    default B anyStructure(String[] idOrTags, boolean exact) {
        return addCondition(LootCondition.anyStructure(idOrTags, exact));
    }

    default B lightLevel(int min, int max) {
        return addCondition(LootCondition.lightLevel(min, max));
    }

    default B killedByPlayer() {
        return addCondition(LootCondition.killedByPlayer());
    }

    default B matchBlockState(Block block, StatePropertiesPredicate.Builder properties) {
        return addCondition(LootCondition.matchBlockState(block, properties));
    }

    default B matchFluid(Resolver resolver) {
        throw new UnsupportedOperationException("Not implemented currently.");
    }

    default B matchEntity(EntityPredicate.Builder entityPredicate) {
        return addCondition(LootCondition.matchEntity(entityPredicate));
    }

    default B matchKiller(EntityPredicate.Builder entityPredicate) {
        return addCondition(LootCondition.matchKiller(entityPredicate));
    }

    default B matchDirectKiller(EntityPredicate.Builder entityPredicate) {
        return addCondition(LootCondition.matchDirectKiller(entityPredicate));
    }

    default B matchPlayer(EntityPredicate.Builder entityPredicate) {
        return addCondition(LootCondition.matchPlayer(entityPredicate));
    }

    default B matchDamageSource(DamageSourcePredicate.Builder predicate) {
        return addCondition(LootCondition.matchDamageSource(predicate));
    }

    default B distance(MinMaxBounds.Doubles bounds) {
        return addCondition(LootCondition.distance(bounds));
    }

    default B customDistance(DistancePredicate predicate) {
        return addCondition(LootCondition.customDistance(predicate));
    }

    default B customPlayerCheck(Predicate<ServerPlayer> predicate) {
        return addCondition(LootCondition.customPlayerCheck(predicate));
    }

    default B customEntityCheck(Predicate<Entity> predicate) {
        return addCondition(LootCondition.customEntityCheck(predicate));
    }

    default B customKillerCheck(Predicate<Entity> predicate) {
        return addCondition(LootCondition.customKillerCheck(predicate));
    }

    default B customDirectKillerCheck(Predicate<Entity> predicate) {
        return addCondition(LootCondition.customDirectKillerCheck(predicate));
    }

    default B blockEntityPredicate(Predicate<BlockEntity> predicate) {
        return addCondition(LootCondition.blockEntity(predicate));
    }

    default B hasAnyStage(String... stages) {
        return addCondition(LootCondition.hasAnyStage(stages));
    }

    default B anyOf(Consumer<LootConditionsContainer<B>> action) {
        List<LootItemCondition> conditions = new ArrayList<>();
        action.accept(new LootConditionsContainer<B>() {
            @Override
            public B addCondition(LootItemCondition condition) {
                conditions.add(condition);
                //noinspection unchecked
                return (B) this;
            }
        });
        LootItemCondition[] array = conditions.toArray(new LootItemCondition[0]);
        return addCondition(LootCondition.anyOf(array));
    }

    default B allOf(Consumer<LootConditionsContainer<B>> action) {
        List<LootItemCondition> conditions = new ArrayList<>();
        action.accept(new LootConditionsContainer<B>() {
            @Override
            public B addCondition(LootItemCondition condition) {
                conditions.add(condition);
                //noinspection unchecked
                return (B) this;
            }
        });
        LootItemCondition[] array = conditions.toArray(new LootItemCondition[0]);
        return addCondition(LootCondition.allOf(array));
    }

    default B jsonCondition(JsonObject json) {
        return addCondition(LootCondition.fromJson(json));
    }

    B addCondition(LootItemCondition condition);
}
