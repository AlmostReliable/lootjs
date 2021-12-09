package com.github.llytho.lootjs.kube;

import dev.latvian.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.function.Predicate;

public class IngredientUtils {
    @SuppressWarnings("unchecked")
    public static Predicate<ItemStack>[] toVanillaPredicates(IngredientJS... pIngredients) {
        return (Predicate<ItemStack>[]) Arrays
                .stream(pIngredients)
                .map(IngredientJS::getVanillaPredicate)
                .toArray(Predicate[]::new);
    }

    @HideFromJS
    public static void nonEmptyIngredientCheck(IngredientJS... pIngredients) {
        for (IngredientJS pIngredient : pIngredients) {
            if (pIngredient.isEmpty()) {
                throw new IllegalArgumentException("Given ingredient does not exists or is empty");
            }
        }
    }
}
