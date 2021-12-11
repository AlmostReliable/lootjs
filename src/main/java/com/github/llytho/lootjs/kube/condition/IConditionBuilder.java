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

    default B anyLootTable(Object... objects) {
        List<Pattern> patterns = new ArrayList<>();
        List<ResourceLocation> locations = new ArrayList<>();

        for (Object o : objects) {
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

    default B matchLoot(IngredientJS ingredient) {
        return matchLoot(ingredient, false);
    }

    default B matchLoot(IngredientJS ingredient, boolean exact) {
        IngredientUtils.nonEmptyIngredientCheck(ingredient);
        if (exact) {
            return addCondition(new ContainsLootCondition(ingredient.getVanillaPredicate(),
                    IConditionOp.Predicate::And));
        }
        return addCondition(new ContainsLootCondition(ingredient.getVanillaPredicate(), IConditionOp.Predicate::Or));
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

    default B anyBiome(ResourceLocation... locations) {
        return addCondition(new BiomeCheck(BiomeUtils.findBiomeKeys(locations), IConditionOp.Predicate::Or));
    }

    default B anyBiomeType(String... types) {
        return addCondition(new BiomeTypeCheck(BiomeUtils.findTypes(types), IConditionOp.Predicate::Or));
    }

    default B biomeType(String... types) {
        return addCondition(new BiomeTypeCheck(BiomeUtils.findTypes(types), IConditionOp.Predicate::And));
    }

    default B anyDimension(ResourceLocation... dimensions) {
        return addCondition(new AnyDimension(dimensions));
    }

    default B anyStructure(ResourceLocation... locations) {
        Structure<?>[] structures = BiomeUtils.findStructures(locations);
        return addCondition(new AnyStructure(structures));
    }

    default B lightLevel(int min, int max) {
        return addCondition(new IsLightLevel(min, max));
    }

    default B killedByPlayer() {
        return addCondition(KilledByPlayer.killedByPlayer().build());
    }

    default B not(Consumer<InvertedBuilderJS> action) {
        InvertedBuilderJS builder = new InvertedBuilderJS();
        action.accept(builder);
        return addCondition(builder.build());
    }

    default B any(Consumer<AlternativeBuilderJS> action) {
        AlternativeBuilderJS builder = new AlternativeBuilderJS();
        action.accept(builder);
        return addCondition(builder.build());
    }

    B addCondition(ILootCondition pCondition);

}
