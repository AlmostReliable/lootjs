package com.almostreliable.lootjs.kube;

import com.almostreliable.lootjs.LootJS;
import com.almostreliable.lootjs.core.LootEntry;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

public class LootEntryWrapper {
    public static LootEntry of(Object o) {
        if (o instanceof LootEntry entry) {
            return entry;
        }

        if (o instanceof Ingredient ingredient) {
            return new LootEntry(new LootEntry.RandomIngredientGenerator(ingredient));
        }

        if (o instanceof CharSequence cs) {
            String string = cs.toString();
            if (string.startsWith("#") || string.startsWith("@")) {
                return of(IngredientJS.of(string));
            }
        }

        OutputItem outputItem = OutputItem.of(o);
        int weight = Double.isNaN(outputItem.getChance()) ? 1 : (int) outputItem.getChance();
        return new LootEntry(outputItem.item).withWeight(weight);
    }

    public static LootEntry of(ItemStack in, int count) {
        return of(in.kjs$withCount(count));
    }

    public static LootEntry of(ItemStack in, CompoundTag nbt) {
        return of(in.kjs$withNBT(nbt));
    }

    public static LootEntry of(ItemStack in, int count, CompoundTag nbt) {
        return of(in.kjs$withCount(count).kjs$withNBT(nbt));
    }

    public static LootEntry ofJson(JsonObject json) {
        try {
            var container = LootJS.FUNCTION_GSON.fromJson(json, LootPoolEntryContainer.class);
            return new LootEntry(new LootEntry.VanillaWrappedLootEntry(container));
        } catch (Exception e) {
            LootJS.LOG.error("Failed to parse loot entry: " + json);
            return new LootEntry(ItemStack.EMPTY);
        }
    }

    public static LootEntry withChance(Object o, int chance) {
        return of(o).withChance(chance);
    }
}
