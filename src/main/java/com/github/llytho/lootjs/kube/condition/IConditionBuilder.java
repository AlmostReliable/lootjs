package com.github.llytho.lootjs.kube.condition;

import com.github.llytho.lootjs.condition.*;
import com.github.llytho.lootjs.core.LootContextType;
import com.github.llytho.lootjs.kube.IngredientUtils;
import com.github.llytho.lootjs.util.BiomeUtils;
import dev.latvian.kubejs.item.ingredient.IngredientJS;
import dev.latvian.kubejs.util.UtilsJS;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.conditions.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.structure.Structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
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

    default B matchLoot(IngredientJS pIngredient) {
        return matchLoot(pIngredient, false);
    }

    default B matchLoot(IngredientJS pIngredient, boolean exact) {
        IngredientUtils.nonEmptyIngredientCheck(pIngredient);
        if (exact) {
            return addCondition(new ContainsLootCondition(pIngredient.getVanillaPredicate(),
                    IConditionOp.Predicate::And));
        }
        return addCondition(new ContainsLootCondition(pIngredient.getVanillaPredicate(), IConditionOp.Predicate::Or));
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
        return addCondition(new BiomeCheck(BiomeUtils.findBiomeKeys(pFilters), IConditionOp.Predicate::Or));
    }

    default B anyBiomeType(String... pTypes) {
        return addCondition(new BiomeTypeCheck(BiomeUtils.findTypes(pTypes), IConditionOp.Predicate::Or));
    }

    default B biomeType(String... pTypes) {
        return addCondition(new BiomeTypeCheck(BiomeUtils.findTypes(pTypes), IConditionOp.Predicate::And));
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

    default B killedByPlayer() {
        return addCondition(KilledByPlayer.killedByPlayer().build());
    }

    default B not(Consumer<InvertedBuilderJS> pAction) {
        InvertedBuilderJS builder = new InvertedBuilderJS();
        pAction.accept(builder);
        return addCondition(builder.build());
    }

    default B any(Consumer<AlternativeBuilderJS> pAction) {
        AlternativeBuilderJS builder = new AlternativeBuilderJS();
        pAction.accept(builder);
        return addCondition(builder.build());
    }

    B addCondition(ILootCondition pCondition);

}
