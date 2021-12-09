package com.github.llytho.lootjs.kube;

import com.github.llytho.lootjs.condition.*;
import com.github.llytho.lootjs.core.ICondition;
import com.github.llytho.lootjs.core.LootContextType;
import com.github.llytho.lootjs.util.BiomeUtils;
import dev.latvian.kubejs.item.ingredient.IngredientJS;
import dev.latvian.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
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
import java.util.function.Predicate;
import java.util.regex.Pattern;

public interface IConditionBuilder<B extends IConditionBuilder<?>> {

    default B anyLootTable(Object... pObjects) {
        List<Pattern> patterns = new ArrayList<>();
        List<ResourceLocation> locations = new ArrayList<>();

        for (Object o : pObjects) {
            Pattern pattern = UtilsJS.parseRegex(o);
            if (pattern == null) {
                locations.add(new ResourceLocation((String) o));
            } else {
                patterns.add(pattern);
            }
        }

        return addCondition(new MatchLootTableId(locations.toArray(new ResourceLocation[0]),
                patterns.toArray(new Pattern[0])));
    }

    default B anyLoot(IngredientJS... pIngredients) {
        Arrays.stream(pIngredients).forEach(this::nonEmptyIngredientCheck);

        @SuppressWarnings("unchecked") Predicate<ItemStack>[] predicates = (Predicate<ItemStack>[]) Arrays
                .stream(pIngredients)
                .map(IngredientJS::getVanillaPredicate)
                .toArray(Predicate[]::new);

        return addCondition(new ContainsLootCondition(predicates, ICondition::Or));
    }

    default B loot(IngredientJS... pIngredients) {
        Arrays.stream(pIngredients).forEach(this::nonEmptyIngredientCheck);

        @SuppressWarnings("unchecked") Predicate<ItemStack>[] predicates = (Predicate<ItemStack>[]) Arrays
                .stream(pIngredients)
                .map(IngredientJS::getVanillaPredicate)
                .toArray(Predicate[]::new);

        return addCondition(new ContainsLootCondition(predicates, ICondition::And));
    }

    default B matchMainHand(IngredientJS pIngredient) {
        return addCondition(new MatchEquipmentSlot(EquipmentSlotType.MAINHAND, pIngredient.getVanillaPredicate()));
    }

    default B matchOffHand(IngredientJS pIngredient) {
        return addCondition(new MatchEquipmentSlot(EquipmentSlotType.OFFHAND, pIngredient.getVanillaPredicate()));
    }

    default B matchSlot(EquipmentSlotType pSlot, IngredientJS pIngredient) {
        return addCondition(new MatchEquipmentSlot(pSlot, pIngredient.getVanillaPredicate()));
    }

    default B anyType(LootContextType... pTypes) {
        return addCondition(new IsLootTableType(pTypes));
    }

    default B survivesExplosion() {
        return addCondition(SurvivesExplosion.survivesExplosion().build());
    }

    default B timeCheck(long pPeriod, float pMin, float pMax) {
        return addCondition(new TimeCheck(pPeriod, new RandomValueRange(pMin, pMax)));
    }

    default B timeCheck(float pMin, float pMax) {
        return timeCheck(24000L, pMin, pMax);
    }

    default B weatherCheck(Map<String, Boolean> pMap) {
        Boolean isRaining = pMap.getOrDefault("raining", null);
        Boolean isThundering = pMap.getOrDefault("thundering", null);

        return addCondition(new WeatherCheck(isRaining, isThundering));
    }

    default B randomChance(float pValue) {
        return addCondition(RandomChance.randomChance(pValue).build());
    }

    default B randomChanceWithLooting(float pValue, float pLooting) {
        return addCondition(RandomChanceWithLooting.randomChanceAndLootingBoost(pValue, pLooting).build());
    }

    default B anyBiome(ResourceLocation... pFilters) {
        RegistryKey<Biome>[] registryKeys = BiomeUtils.findBiomeKeys(pFilters);
        return addCondition(new BiomeCheck(registryKeys, ICondition::Or));
    }

    default B anyBiomeType(String... pTypes) {
        BiomeDictionary.Type[] types = BiomeUtils.findTypes(pTypes);
        return addCondition(new BiomeTypeCheck(types, ICondition::Or));
    }

    default B biomeType(String... pTypes) {
        BiomeDictionary.Type[] types = BiomeUtils.findTypes(pTypes);
        return addCondition(new BiomeTypeCheck(types, ICondition::And));
    }

    default B anyDimension(ResourceLocation... pDimensions) {
        return addCondition(new AnyDimension(pDimensions));
    }

    default B anyStructure(ResourceLocation... pStructures) {
        Structure<?>[] structures = BiomeUtils.findStructures(pStructures);
        return addCondition(new AnyStructure(structures));
    }

    default B lightLevel(int min, int max) {
        return addCondition(new IsLightLevel(min, max));
    }

    B addCondition(Predicate<LootContext> pCondition);

    @HideFromJS
    default void nonEmptyIngredientCheck(IngredientJS pIngredient) {
        if (pIngredient.isEmpty()) {
            throw new IllegalArgumentException("Given ingredient does not exists or is empty");
        }
    }
}
