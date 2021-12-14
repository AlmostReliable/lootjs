package com.github.llytho.lootjs.kube;

import com.github.llytho.lootjs.core.LootContextType;
import com.github.llytho.lootjs.kube.builder.AlternativeConditionBuilderJS;
import com.github.llytho.lootjs.kube.builder.DamageSourcePredicateBuilderJS;
import com.github.llytho.lootjs.kube.builder.EntityPredicateBuilderJS;
import com.github.llytho.lootjs.kube.builder.InvertedConditionBuilderJS;
import com.github.llytho.lootjs.loot.condition.*;
import com.github.llytho.lootjs.util.BiomeUtils;
import com.google.gson.JsonObject;
import dev.latvian.kubejs.item.ingredient.IngredientJS;
import dev.latvian.kubejs.util.UtilsJS;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.conditions.*;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public interface ConditionsContainer<B extends ConditionsContainer<?>> {

    default B anyLootTable(String... idsOrRegex) {
        List<Pattern> patterns = new ArrayList<>();
        List<ResourceLocation> locations = new ArrayList<>();

        for (String str : idsOrRegex) {
            Pattern pattern = UtilsJS.parseRegex(str);
            if (pattern == null) {
                locations.add(new ResourceLocation(str));
            } else {
                patterns.add(pattern);
            }
        }

        return addCondition(new MatchLootTableId(locations.toArray(new ResourceLocation[0]),
                patterns.toArray(new Pattern[0])));
    }

    default B matchLoot(IngredientJS ingredient) {
        return matchLoot(ingredient, false);
    }

    default B matchLoot(IngredientJS ingredient, boolean exact) {
        return addCondition(new ContainsLootCondition(ingredient.getVanillaPredicate(), exact));
    }

    default B matchMainHand(IngredientJS ingredient) {
        return addCondition(new MatchEquipmentSlot(EquipmentSlotType.MAINHAND, ingredient.getVanillaPredicate()));
    }

    default B matchOffHand(IngredientJS ingredient) {
        return addCondition(new MatchEquipmentSlot(EquipmentSlotType.OFFHAND, ingredient.getVanillaPredicate()));
    }

    default B matchSlot(EquipmentSlotType slot, IngredientJS ingredient) {
        return addCondition(new MatchEquipmentSlot(slot, ingredient.getVanillaPredicate()));
    }

    default B anyType(LootContextType... types) {
        return addCondition(new IsLootTableType(types));
    }

    default B survivesExplosion() {
        return addCondition(SurvivesExplosion.survivesExplosion().build());
    }

    default B timeCheck(long period, float min, float max) {
        return addCondition(new TimeCheck(period, new RandomValueRange(min, max)));
    }

    default B timeCheck(float min, float max) {
        return timeCheck(24000L, min, max);
    }

    default B weatherCheck(Map<String, Boolean> map) {
        Boolean isRaining = map.getOrDefault("raining", null);
        Boolean isThundering = map.getOrDefault("thundering", null);

        return addCondition(new WeatherCheck(isRaining, isThundering));
    }

    default B randomChance(float value) {
        return addCondition(RandomChance.randomChance(value).build());
    }

    default B randomChanceWithLooting(float value, float looting) {
        return addCondition(RandomChanceWithLooting.randomChanceAndLootingBoost(value, looting).build());
    }

    default B biome(String... biomesOrTags) {
        Map<Boolean, List<String>> lists = Arrays
                .stream(biomesOrTags)
                .collect(Collectors.partitioningBy(s -> s.startsWith("#")));

        List<RegistryKey<Biome>> biomeKeys = BiomeUtils.findBiomeKeys(lists
                .get(true)
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

        List<RegistryKey<Biome>> biomeKeys = BiomeUtils.findBiomeKeys(lists
                .get(true)
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

    default B anyStructure(ResourceLocation... locations) {
        Structure<?>[] structures = BiomeUtils.findStructures(Arrays.asList(locations));
        return addCondition(new AnyStructure(structures));
    }

    default B lightLevel(int min, int max) {
        return addCondition(new IsLightLevel(min, max));
    }

    default B killedByPlayer() {
        return addCondition(KilledByPlayer.killedByPlayer().build());
    }


    default B matchEntity(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS builder = new EntityPredicateBuilderJS();
        action.accept(builder);
        return addCondition(new EntityHasProperty(builder.build(), LootContext.EntityTarget.THIS));
    }

    default B matchKillerEntity(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS builder = new EntityPredicateBuilderJS();
        action.accept(builder);
        return addCondition(new EntityHasProperty(builder.build(), LootContext.EntityTarget.KILLER));
    }

    default B matchDirectKillerEntity(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS builder = new EntityPredicateBuilderJS();
        action.accept(builder);
        return addCondition(new EntityHasProperty(builder.build(), LootContext.EntityTarget.DIRECT_KILLER));
    }

    default B matchLastDamagePlayer(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS builder = new EntityPredicateBuilderJS();
        action.accept(builder);
        return addCondition(new EntityHasProperty(builder.build(), LootContext.EntityTarget.KILLER_PLAYER));
    }

    default B matchDamageSource(Consumer<DamageSourcePredicateBuilderJS> action) {
        DamageSourcePredicateBuilderJS builder = new DamageSourcePredicateBuilderJS();
        action.accept(builder);
        return addCondition(new DamageSourceProperties(builder.build()));
    }

    default B not(Consumer<InvertedConditionBuilderJS> action) {
        InvertedConditionBuilderJS builder = new InvertedConditionBuilderJS();
        action.accept(builder);
        return addCondition(builder.build());
    }

    default B any(Consumer<AlternativeConditionBuilderJS> action) {
        AlternativeConditionBuilderJS builder = new AlternativeConditionBuilderJS();
        action.accept(builder);
        return addCondition(builder.build());
    }

    default B customCondition(JsonObject json) {
        ILootCondition condition = LootTableManager.GSON.fromJson(json, ILootCondition.class);
        return addCondition(condition);
    }

    B addCondition(ILootCondition pCondition);

}
