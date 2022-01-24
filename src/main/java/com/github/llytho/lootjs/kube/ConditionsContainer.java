package com.github.llytho.lootjs.kube;

import com.github.llytho.lootjs.kube.builder.*;
import com.github.llytho.lootjs.loot.condition.*;
import com.github.llytho.lootjs.loot.condition.builder.DistancePredicateBuilder;
import com.github.llytho.lootjs.util.BiomeUtils;
import com.github.llytho.lootjs.util.TagOrEntry;
import com.github.llytho.lootjs.util.Utils;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import net.minecraft.advancements.critereon.FluidPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.PredicateManager;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public interface ConditionsContainer<B extends ConditionsContainer<?>> {

    default B matchLoot(IngredientJS ingredient) {
        return matchLoot(ingredient, false);
    }

    default B matchLoot(IngredientJS ingredient, boolean exact) {
        return addCondition(new ContainsLootCondition(ingredient.getVanillaPredicate(), exact));
    }

    default B matchMainHand(IngredientJS ingredient) {
        return addCondition(new MatchEquipmentSlot(EquipmentSlot.MAINHAND, ingredient.getVanillaPredicate()));
    }

    default B matchOffHand(IngredientJS ingredient) {
        return addCondition(new MatchEquipmentSlot(EquipmentSlot.OFFHAND, ingredient.getVanillaPredicate()));
    }

    default B matchEquip(EquipmentSlot slot, IngredientJS ingredient) {
        return addCondition(new MatchEquipmentSlot(slot, ingredient.getVanillaPredicate()));
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

    default B matchFluid(String idOrTag) {
        TagOrEntry<Fluid> tagOrEntry = Utils.getTagOrEntry(ForgeRegistries.FLUIDS, idOrTag);
        FluidPredicate predicate = new FluidPredicate(tagOrEntry.tag, tagOrEntry.entry, StatePropertiesPredicate.ANY);
        return addCondition(new MatchFluid(predicate));
    }

    default B matchEntity(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS builder = new EntityPredicateBuilderJS();
        action.accept(builder);
        return addCondition(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, builder.build()));
    }

    default B matchKiller(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS builder = new EntityPredicateBuilderJS();
        action.accept(builder);
        return addCondition(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.KILLER, builder.build()));
    }

    default B matchDirectKiller(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS builder = new EntityPredicateBuilderJS();
        action.accept(builder);
        return addCondition(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.DIRECT_KILLER, builder.build()));
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
        return customDistanceToPlayer(builder -> {
            builder.absolute(bounds);
        });
    }

    default B customDistanceToPlayer(Consumer<DistancePredicateBuilder> action) {
        DistancePredicateBuilder builder = new DistancePredicateBuilder();
        action.accept(builder);
        return addCondition(new MatchKillerDistance(builder.build()));
    }

    default B not(Consumer<NotConditionBuilder> action) {
        NotConditionBuilder builder = new NotConditionBuilder();
        action.accept(builder);
        return addCondition(builder.build());
    }

    default B or(Consumer<OrConditionBuilder> action) {
        OrConditionBuilder builder = new OrConditionBuilder();
        action.accept(builder);
        return addCondition(builder.build());
    }

    default B and(Consumer<AndConditionBuilder> action) {
        AndConditionBuilder builder = new AndConditionBuilder();
        action.accept(builder);
        return addCondition(builder.build());
    }

    default B customCondition(JsonObject json) {
        LootItemCondition condition = PredicateManager.GSON.fromJson(json, LootItemCondition.class);
        return addCondition(condition);
    }

    default B addCondition(LootItemCondition.Builder builder) {
        return addCondition(builder.build());
    }

    B addCondition(LootItemCondition pCondition);
}
