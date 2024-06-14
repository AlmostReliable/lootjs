package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.core.filters.Resolver;
import com.almostreliable.lootjs.loot.condition.*;
import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class LootCondition {
    public static LootItemCondition matchTool(ItemPredicate predicate) {
        return new MatchTool(Optional.of(predicate));
    }

    public static LootItemCondition matchMainHand(ItemFilter filter) {
        return new MatchEquipmentSlot(EquipmentSlot.MAINHAND, filter);
    }

    public static LootItemCondition matchOffHand(ItemFilter filter) {
        return new MatchEquipmentSlot(EquipmentSlot.OFFHAND, filter);
    }

    public static LootItemCondition matchHead(ItemFilter filter) {
        return new MatchEquipmentSlot(EquipmentSlot.HEAD, filter);
    }

    public static LootItemCondition matchChest(ItemFilter filter) {
        return new MatchEquipmentSlot(EquipmentSlot.CHEST, filter);
    }

    public static LootItemCondition matchLegs(ItemFilter filter) {
        return new MatchEquipmentSlot(EquipmentSlot.LEGS, filter);
    }

    public static LootItemCondition matchFeet(ItemFilter filter) {
        return new MatchEquipmentSlot(EquipmentSlot.FEET, filter);
    }

    public static LootItemCondition matchEquip(EquipmentSlot slot, ItemFilter filter) {
        return new MatchEquipmentSlot(slot, filter);
    }

    public static LootItemCondition survivesExplosion() {
        return ExplosionCondition.survivesExplosion().build();
    }

    public static LootItemCondition timeCheck(long period, int min, int max) {
        return new TimeCheck.Builder(IntRange.range(min, max)).setPeriod(period).build();
    }

    public static LootItemCondition timeCheck(int min, int max) {
        return timeCheck(24000L, min, max);
    }

    public static LootItemCondition weatherCheck(@Nullable Boolean raining, @Nullable Boolean thundering) {
        WeatherCheck.Builder builder = new WeatherCheck.Builder();
        if (raining != null) builder.setRaining(raining);
        if (thundering != null) builder.setThundering(thundering);
        return builder.build();
    }

    public static LootItemCondition randomChance(float value) {
        return LootItemRandomChanceCondition.randomChance(value).build();
    }

    public static LootItemCondition randomChanceWithEnchantment(Holder<Enchantment> enchantment, float[] chances) {
        Preconditions.checkArgument(chances.length > 0, "There must be at least one chance");
        float first = chances[0];
        List<Float> enchantmentChances = new ArrayList<>();
        for (int i = 1; i < chances.length; i++) {
            enchantmentChances.add(chances[i]);
        }
        LevelBasedValue.Lookup lookup = new LevelBasedValue.Lookup(enchantmentChances, new LevelBasedValue.Constant(0));
        return new LootItemRandomChanceWithEnchantedBonusCondition(first, lookup, enchantment);
    }

    public static LootItemCondition randomTableBonus(Holder<Enchantment> enchantment, float[] chances) {
        return BonusLevelTableCondition.bonusLevelFlatChance(enchantment, chances).build();
    }

    public static LootItemCondition location(LocationPredicate.Builder predicate) {
        return LocationCheck.checkLocation(predicate).build();
    }

    public static LootItemCondition location(BlockPos offset, LocationPredicate.Builder predicate) {
        return LocationCheck.checkLocation(predicate, offset).build();
    }

    public static LootItemCondition biome(Resolver... resolvers) {
        List<ResourceKey<Biome>> biomes = new ArrayList<>();
        List<TagKey<Biome>> tagKeys = new ArrayList<>();

        for (Resolver resolver : resolvers) {
            if (resolver instanceof Resolver.ByEntry byEntry) {
                biomes.add(byEntry.resolve(Registries.BIOME));
            } else if (resolver instanceof Resolver.ByTagKey byTagKey) {
                tagKeys.add(byTagKey.resolve(Registries.BIOME));
            }
        }

        return new BiomeCheck(biomes, tagKeys);
    }

    public static LootItemCondition anyBiome(Resolver... resolvers) {
        List<ResourceKey<Biome>> biomes = new ArrayList<>();
        List<TagKey<Biome>> tagKeys = new ArrayList<>();

        for (Resolver resolver : resolvers) {
            if (resolver instanceof Resolver.ByEntry byEntry) {
                biomes.add(byEntry.resolve(Registries.BIOME));
            } else if (resolver instanceof Resolver.ByTagKey byTagKey) {
                tagKeys.add(byTagKey.resolve(Registries.BIOME));
            }
        }
        return new AnyBiomeCheck(biomes, tagKeys);
    }

    public static LootItemCondition anyDimension(ResourceLocation... dimensions) {
        return new AnyDimension(dimensions);
    }

    public static LootItemCondition anyStructure(String[] idOrTags, boolean exact) {
        AnyStructure.Builder builder = new AnyStructure.Builder();
        for (String s : idOrTags) {
            builder.add(s);
        }
        return builder.build(exact);
    }

    public static LootItemCondition lightLevel(int min, int max) {
        return new IsLightLevel(min, max);
    }

    public static LootItemCondition luck(MinMaxBounds.Doubles bounds) {
        return customPlayerCheck(serverPlayer -> bounds.matches(serverPlayer.getLuck()));
    }

    public static LootItemCondition killedByPlayer() {
        return LootItemKilledByPlayerCondition.killedByPlayer().build();
    }

    public static LootItemCondition matchBlockState(Block block, StatePropertiesPredicate.Builder properties) {
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(properties).build();
    }

    public static LootItemCondition matchEntity(EntityPredicate.Builder entityPredicate) {
        return LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, entityPredicate).build();
    }

    public static LootItemCondition matchKiller(EntityPredicate.Builder entityPredicate) {
        return LootItemEntityPropertyCondition
                .hasProperties(LootContext.EntityTarget.ATTACKER, entityPredicate)
                .build();
    }

    public static LootItemCondition matchDirectKiller(EntityPredicate.Builder entityPredicate) {
        return LootItemEntityPropertyCondition
                .hasProperties(LootContext.EntityTarget.DIRECT_ATTACKER, entityPredicate)
                .build();
    }

    public static LootItemCondition matchPlayer(EntityPredicate.Builder entityPredicate) {
        return new MatchPlayer(entityPredicate.build());
    }

    public static LootItemCondition matchDamageSource(DamageSourcePredicate.Builder predicate) {
        return DamageSourceCondition.hasDamageSource(predicate).build();
    }

    public static LootItemCondition distance(MinMaxBounds.Doubles bounds) {
        return customDistance(new DistancePredicate(MinMaxBounds.Doubles.ANY,
                MinMaxBounds.Doubles.ANY,
                MinMaxBounds.Doubles.ANY,
                MinMaxBounds.Doubles.ANY,
                bounds));
    }

    public static LootItemCondition customDistance(DistancePredicate predicate) {
        return new MatchKillerDistance(predicate);
    }

    public static LootItemCondition customPlayerCheck(Predicate<ServerPlayer> predicate) {
        return new PlayerParamPredicate(predicate);
    }

    public static LootItemCondition customEntityCheck(Predicate<Entity> predicate) {
        return new CustomParamPredicate<>(LootContextParams.THIS_ENTITY, predicate);
    }

    public static LootItemCondition customKillerCheck(Predicate<Entity> predicate) {
        return new CustomParamPredicate<>(LootContextParams.ATTACKING_ENTITY, predicate);
    }

    public static LootItemCondition customDirectKillerCheck(Predicate<Entity> predicate) {
        return new CustomParamPredicate<>(LootContextParams.DIRECT_ATTACKING_ENTITY, predicate);
    }

    public static LootItemCondition blockEntity(Predicate<BlockEntity> predicate) {
        return new CustomParamPredicate<>(LootContextParams.BLOCK_ENTITY, predicate);
    }

    public static LootItemCondition hasAnyStage(String... stages) {
        if (stages.length == 1) {
            String stage = stages[0];
            return new PlayerParamPredicate((player) -> player.getTags().contains(stage));
        }

        return new PlayerParamPredicate((player) -> {
            for (String stage : stages) {
                if (player.getTags().contains(stage)) {
                    return true;
                }
            }
            return false;
        });
    }

    public static LootItemCondition fromJson(JsonObject json) {
        return LootItemCondition.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow().value();
    }

    public static LootItemCondition matchAnyOf(LootItemCondition... conditions) {
        return new AnyOfCondition(Arrays.asList(conditions));
    }

    public static LootItemCondition matchAllOf(LootItemCondition... conditions) {
        return new AllOfCondition(Arrays.asList(conditions));
    }
}
