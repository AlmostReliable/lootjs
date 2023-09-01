package com.almostreliable.lootjs.kube;

import com.almostreliable.lootjs.LootJS;
import com.almostreliable.lootjs.core.ILootCondition;
import com.almostreliable.lootjs.filters.ItemFilter;
import com.almostreliable.lootjs.filters.Resolver;
import com.almostreliable.lootjs.kube.builder.DamageSourcePredicateBuilderJS;
import com.almostreliable.lootjs.kube.builder.EntityPredicateBuilderJS;
import com.almostreliable.lootjs.loot.condition.*;
import com.almostreliable.lootjs.loot.condition.builder.DistancePredicateBuilder;
import com.almostreliable.lootjs.util.Utils;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.stages.Stages;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings({ "UnusedReturnValue", "unused" })
public interface LootConditionsContainer<B extends LootConditionsContainer<?>> {

    default B matchLoot(ItemFilter filter) {
        return matchLoot(filter, false);
    }

    default B matchLoot(ItemFilter filter, boolean exact) {
        return addCondition(new ContainsLootCondition(filter, exact));
    }

    default B matchMainHand(ItemFilter filter) {
        return addCondition(new MatchEquipmentSlot(EquipmentSlot.MAINHAND, filter));
    }

    default B matchOffHand(ItemFilter filter) {
        return addCondition(new MatchEquipmentSlot(EquipmentSlot.OFFHAND, filter));
    }

    default B matchEquip(EquipmentSlot slot, ItemFilter filter) {
        return addCondition(new MatchEquipmentSlot(slot, filter));
    }

    default B survivesExplosion() {
        return addCondition(ExplosionCondition.survivesExplosion());
    }

    default B timeCheck(long period, int min, int max) {
        return addCondition(new TimeCheck.Builder(IntRange.range(min, max)).setPeriod(period));
    }

    default B timeCheck(int min, int max) {
        return timeCheck(24000L, min, max);
    }

    default B weatherCheck(Map<String, Boolean> map) {
        Boolean isRaining = map.getOrDefault("raining", null);
        Boolean isThundering = map.getOrDefault("thundering", null);
        return addCondition(new WeatherCheck.Builder().setRaining(isRaining).setThundering(isThundering));
    }

    default B randomChance(float value) {
        return addCondition(LootItemRandomChanceCondition.randomChance(value));
    }

    default B randomChanceWithLooting(float value, float looting) {
        // wtf are this class names
        return addCondition(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(value, looting));
    }

    default B randomChanceWithEnchantment(@Nullable Enchantment enchantment, float[] chances) {
        if (enchantment == null) {
            throw new IllegalArgumentException("Enchant not found");
        }

        return addCondition(new MainHandTableBonus(enchantment, chances));
    }

    default B biome(Resolver... resolvers) {
        List<ResourceKey<Biome>> biomes = new ArrayList<>();
        List<TagKey<Biome>> tagKeys = new ArrayList<>();

        for (Resolver resolver : resolvers) {
            if (resolver instanceof Resolver.ByEntry byEntry) {
                biomes.add(byEntry.resolve(Registries.BIOME));
            } else if (resolver instanceof Resolver.ByTagKey byTagKey) {
                tagKeys.add(byTagKey.resolve(Registries.BIOME));
            }
        }

        return addCondition(new BiomeCheck(biomes, tagKeys));
    }

    default B anyBiome(Resolver... resolvers) {
        List<ResourceKey<Biome>> biomes = new ArrayList<>();
        List<TagKey<Biome>> tagKeys = new ArrayList<>();

        for (Resolver resolver : resolvers) {
            if (resolver instanceof Resolver.ByEntry byEntry) {
                biomes.add(byEntry.resolve(Registries.BIOME));
            } else if (resolver instanceof Resolver.ByTagKey byTagKey) {
                tagKeys.add(byTagKey.resolve(Registries.BIOME));
            }
        }
        return addCondition(new AnyBiomeCheck(biomes, tagKeys));
    }

    default B anyDimension(ResourceLocation... dimensions) {
        return addCondition(new AnyDimension(dimensions));
    }

    default B anyStructure(String[] idOrTags, boolean exact) {
        AnyStructure.Builder builder = new AnyStructure.Builder();
        for (String s : idOrTags) {
            builder.add(s);
        }
        return addCondition(builder.build(exact));
    }

    default B lightLevel(int min, int max) {
        return addCondition(new IsLightLevel(min, max));
    }

    default B killedByPlayer() {
        return addCondition(LootItemKilledByPlayerCondition.killedByPlayer());
    }

    default B matchBlockState(Block block, Map<String, String> propertyMap) {
        StatePropertiesPredicate.Builder properties = Utils.createProperties(block, propertyMap);
        return addCondition(new LootItemBlockStatePropertyCondition.Builder(block).setProperties(properties));
    }

    default B matchFluid(Resolver resolver) {
        throw new UnsupportedOperationException("Not implemented in 1.18.2 currently.");
    }

    default B matchEntity(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS builder = new EntityPredicateBuilderJS();
        action.accept(builder);
        return addCondition(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                builder.build()));
    }

    default B matchKiller(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS builder = new EntityPredicateBuilderJS();
        action.accept(builder);
        return addCondition(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.KILLER,
                builder.build()));
    }

    default B matchDirectKiller(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS builder = new EntityPredicateBuilderJS();
        action.accept(builder);
        return addCondition(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.DIRECT_KILLER,
                builder.build()));
    }

    default B matchPlayer(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS builder = new EntityPredicateBuilderJS();
        action.accept(builder);
        return addCondition(new MatchPlayer(builder.build()));
    }

    default B matchDamageSource(Consumer<DamageSourcePredicateBuilderJS> action) {
        DamageSourcePredicateBuilderJS builder = new DamageSourcePredicateBuilderJS();
        action.accept(builder);
        return addCondition(builder);
    }

    default B distanceToKiller(MinMaxBounds.Doubles bounds) {
        return customDistanceToPlayer(builder -> builder.absolute(bounds));
    }

    default B customDistanceToPlayer(Consumer<DistancePredicateBuilder> action) {
        DistancePredicateBuilder builder = new DistancePredicateBuilder();
        action.accept(builder);
        return addCondition(new MatchKillerDistance(builder.build()));
    }

    default B playerPredicate(Predicate<ServerPlayer> predicate) {
        return addCondition(new PlayerParamPredicate(predicate));
    }

    default B entityPredicate(Predicate<Entity> predicate) {
        return addCondition(new CustomParamPredicate<>(LootContextParams.THIS_ENTITY, predicate));
    }

    default B killerPredicate(Predicate<Entity> predicate) {
        return addCondition(new CustomParamPredicate<>(LootContextParams.KILLER_ENTITY, predicate));
    }

    default B directKillerPredicate(Predicate<Entity> predicate) {
        return addCondition(new CustomParamPredicate<>(LootContextParams.DIRECT_KILLER_ENTITY, predicate));
    }

    default B blockEntityPredicate(Predicate<BlockEntity> predicate) {
        return addCondition(new CustomParamPredicate<>(LootContextParams.BLOCK_ENTITY, predicate));
    }

    default B hasAnyStage(String... stages) {
        if (stages.length == 1) {
            String stage = stages[0];
            return addCondition(new PlayerParamPredicate((player) -> Stages.get(player).has(stage)));
        }

        return addCondition(new PlayerParamPredicate((player) -> {
            for (String stage : stages) {
                if (Stages.get(player).has(stage)) {
                    return true;
                }
            }
            return false;
        }));
    }

    default B not(Consumer<LootConditionsContainer<B>> action) {
        List<ILootCondition> conditions = createConditions(action);
        if (conditions.size() != 1) {
            throw new IllegalArgumentException("You only can have one condition for `not`");
        }
        NotCondition condition = new NotCondition(conditions.get(0));
        return addCondition(condition);
    }

    default B or(Consumer<LootConditionsContainer<B>> action) {
        List<ILootCondition> conditions = createConditions(action);
        ILootCondition[] array = conditions.toArray(new ILootCondition[0]);
        return addCondition(new OrCondition(array));
    }

    default B and(Consumer<LootConditionsContainer<B>> action) {
        List<ILootCondition> conditions = createConditions(action);
        ILootCondition[] array = conditions.toArray(new ILootCondition[0]);
        return addCondition(new AndCondition(array));
    }

    default List<ILootCondition> createConditions(Consumer<LootConditionsContainer<B>> action) {
        List<ILootCondition> conditions = new ArrayList<>();
        LootConditionsContainer<B> container = new LootConditionsContainer<B>() {
            @Override
            public B addCondition(ILootCondition condition) {
                conditions.add(condition);
                //noinspection unchecked
                return (B) this;
            }
        };
        action.accept(container);
        return conditions;
    }

    default B customCondition(JsonObject json) {
        LootItemCondition condition = LootJS.GSON.fromJson(json, LootItemCondition.class);
        return addCondition((ILootCondition) condition);
    }

    default B addCondition(LootItemCondition.Builder builder) {
        return addCondition((ILootCondition) builder.build());
    }

    B addCondition(ILootCondition condition);
}
