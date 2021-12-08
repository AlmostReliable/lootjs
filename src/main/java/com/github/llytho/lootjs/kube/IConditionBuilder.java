package com.github.llytho.lootjs.kube;

import com.github.llytho.lootjs.condition.ContainsAllLoot;
import com.github.llytho.lootjs.condition.ContainsAnyLoot;
import com.github.llytho.lootjs.condition.IsLootTableType;
import com.github.llytho.lootjs.condition.MatchLootTableId;
import com.github.llytho.lootjs.core.LootContextType;
import com.github.llytho.lootjs.kube.condition.MatchSlotJS;
import dev.latvian.kubejs.item.ItemStackJS;
import dev.latvian.kubejs.item.ingredient.IngredientJS;
import dev.latvian.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.conditions.*;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public interface IConditionBuilder<B extends IConditionBuilder<?>> {

    default B lootTable(Object... pObjects) {
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

    default B containsAnyLoot(IngredientJS pIngredient) {
        nonEmptyIngredientCheck(pIngredient);

        ItemStack[] vanillaStacks = pIngredient
                .getStacks()
                .stream()
                .map(ItemStackJS::getItemStack)
                .toArray(ItemStack[]::new);

        return addCondition(new ContainsAnyLoot(vanillaStacks));
    }

    default B containsAllLoot(IngredientJS pIngredient) {
        nonEmptyIngredientCheck(pIngredient);

        ItemStack[] vanillaStacks = pIngredient
                .getStacks()
                .stream()
                .map(ItemStackJS::getItemStack)
                .toArray(ItemStack[]::new);

        return addCondition(new ContainsAllLoot(vanillaStacks));
    }

    default B matchMainHand(IngredientJS pIngredient) {
        return addCondition(new MatchSlotJS(EquipmentSlotType.MAINHAND, pIngredient));
    }

    default B matchOffHand(IngredientJS pIngredient) {
        return addCondition(new MatchSlotJS(EquipmentSlotType.OFFHAND, pIngredient));
    }

    default B matchSlot(EquipmentSlotType pSlot, IngredientJS pIngredient) {
        return addCondition(new MatchSlotJS(pSlot, pIngredient));
    }

    default B type(LootContextType... pTypes) {
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

    B addCondition(Predicate<LootContext> pCondition);

    @HideFromJS
    default void nonEmptyIngredientCheck(IngredientJS pIngredient) {
        if (pIngredient.isEmpty()) {
            throw new IllegalArgumentException("Given pIngredient does not exists or is empty");
        }
    }
}
