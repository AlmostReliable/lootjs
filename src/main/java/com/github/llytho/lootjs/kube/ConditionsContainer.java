package com.github.llytho.lootjs.kube;

import com.github.llytho.lootjs.core.ILootCondition;
import com.github.llytho.lootjs.filters.ItemFilter;
import com.github.llytho.lootjs.filters.TagKeyOrEntryResolver;
import com.github.llytho.lootjs.kube.builder.DamageSourcePredicateBuilderJS;
import com.github.llytho.lootjs.kube.builder.EntityPredicateBuilderJS;
import com.github.llytho.lootjs.loot.condition.*;
import com.github.llytho.lootjs.loot.condition.builder.DistancePredicateBuilder;
import com.github.llytho.lootjs.util.BiomeUtils;
import com.github.llytho.lootjs.util.Utils;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.entity.EntityJS;
import dev.latvian.mods.kubejs.player.PlayerJS;
import dev.latvian.mods.kubejs.stages.Stages;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.PredicateManager;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings({ "UnusedReturnValue", "unused" })
public interface ConditionsContainer<B extends ConditionsContainer<?>> {

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

    default B biome(String... biomesOrTags) {
        Map<Boolean, List<String>> lists = Arrays
                .stream(biomesOrTags)
                .collect(Collectors.partitioningBy(s -> s.startsWith("#")));

        List<ResourceKey<Biome>> biomeKeys = BiomeUtils.findBiomeKeys(lists
                .get(false)
                .stream()
                .map(ResourceLocation::new)
                .collect(Collectors.toList()));
        List<BiomeDictionary.Type> types = BiomeUtils.findTypes(lists
                .get(true)
                .stream()
                .map(s -> s.substring(1))
                .collect(Collectors.toList()));

        return addCondition(new BiomeCheck(biomeKeys, types));
    }

    default B anyBiome(String... biomesOrTags) {
        Map<Boolean, List<String>> lists = Arrays
                .stream(biomesOrTags)
                .collect(Collectors.partitioningBy(s -> s.startsWith("#")));

        List<ResourceKey<Biome>> biomeKeys = BiomeUtils.findBiomeKeys(lists
                .get(false)
                .stream()
                .map(ResourceLocation::new)
                .collect(Collectors.toList()));
        List<BiomeDictionary.Type> types = BiomeUtils.findTypes(lists
                .get(true)
                .stream()
                .map(s -> s.substring(1))
                .collect(Collectors.toList()));

        return addCondition(new AnyBiomeCheck(biomeKeys, types));
    }

    default B anyDimension(ResourceLocation... dimensions) {
        return addCondition(new AnyDimension(dimensions));
    }

    default B anyStructure(ResourceLocation[] locations, boolean exact) {
        StructureFeature<?>[] structures = BiomeUtils.findStructures(Arrays.asList(locations));
        return addCondition(new AnyStructure(structures, exact));
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

    default B matchFluid(TagKeyOrEntryResolver resolver) {
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

    default B playerPredicate(Predicate<PlayerJS<?>> predicate) {
        return addCondition(new PlayerParamPredicate((ctx, player) -> {
            PlayerJS<?> p = UtilsJS.getLevel(ctx.getLevel()).getPlayer(player);
            return p != null && predicate.test(p);
        }));
    }

    default B entityPredicate(Predicate<EntityJS> predicate) {
        return addCondition(new CustomParamPredicate<>(LootContextParams.THIS_ENTITY, (ctx, entity) -> {
            EntityJS e = UtilsJS.getLevel(ctx.getLevel()).getEntity(entity);
            return e != null && predicate.test(e);
        }));
    }

    default B killerPredicate(Predicate<EntityJS> predicate) {
        return addCondition(new CustomParamPredicate<>(LootContextParams.KILLER_ENTITY, (ctx, entity) -> {
            EntityJS e = UtilsJS.getLevel(ctx.getLevel()).getEntity(entity);
            return e != null && predicate.test(e);
        }));
    }

    default B directKillerPredicate(Predicate<EntityJS> predicate) {
        return addCondition(new CustomParamPredicate<>(LootContextParams.DIRECT_KILLER_ENTITY, (ctx, entity) -> {
            EntityJS e = UtilsJS.getLevel(ctx.getLevel()).getEntity(entity);
            return e != null && predicate.test(e);
        }));
    }

    default B hasAnyStage(String... stages) {
        if (stages.length == 1) {
            String stage = stages[0];
            return addCondition(new PlayerParamPredicate((ctx, player) -> Stages.get(player).has(stage)));
        }

        return addCondition(new PlayerParamPredicate((ctx, player) -> {
            for (String stage : stages) {
                if (Stages.get(player).has(stage)) {
                    return true;
                }
            }
            return false;
        }));
    }

    default B not(Consumer<ConditionsContainer<B>> action) {
        List<ILootCondition> conditions = createConditions(action);
        if (conditions.size() != 1) {
            throw new IllegalArgumentException("You only can have one condition for `not`");
        }
        NotCondition condition = new NotCondition(conditions.get(0));
        return addCondition(condition);
    }

    default B or(Consumer<ConditionsContainer<B>> action) {
        List<ILootCondition> conditions = createConditions(action);
        ILootCondition[] array = conditions.toArray(new ILootCondition[0]);
        return addCondition(new OrCondition(array));
    }

    default B and(Consumer<ConditionsContainer<B>> action) {
        List<ILootCondition> conditions = createConditions(action);
        ILootCondition[] array = conditions.toArray(new ILootCondition[0]);
        return addCondition(new AndCondition(array));
    }

    default List<ILootCondition> createConditions(Consumer<ConditionsContainer<B>> action) {
        List<ILootCondition> conditions = new ArrayList<>();
        ConditionsContainer<B> container = new ConditionsContainer<B>() {
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
        LootItemCondition condition = PredicateManager.GSON.fromJson(json, LootItemCondition.class);
        return addCondition((ILootCondition) condition);
    }

    default B addCondition(LootItemCondition.Builder builder) {
        return addCondition((ILootCondition) builder.build());
    }

    B addCondition(ILootCondition condition);
}
