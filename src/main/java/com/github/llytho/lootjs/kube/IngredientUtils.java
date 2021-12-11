package com.github.llytho.lootjs.kube;

import dev.latvian.kubejs.item.ingredient.IngredientJS;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class IngredientUtils {

    public static List<Predicate<ItemStack>> toVanillaPredicates(IngredientJS... ingredients) {
        ArrayList<Predicate<ItemStack>> result = new ArrayList<>();
        for (IngredientJS pIngredient : ingredients) {
            result.add(pIngredient.getVanillaPredicate());
        }
        return result;
    }

    public static void nonEmptyIngredientCheck(IngredientJS... ingredients) {
        for (IngredientJS pIngredient : ingredients) {
            if (pIngredient.isEmpty()) {
                throw new IllegalArgumentException("Given ingredient does not exists or is empty");
            }
        }
    }
}
