package com.almostreliable.lootjs.kube;

import com.almostreliable.lootjs.loot.table.entry.CompositeLootEntry;
import com.almostreliable.lootjs.loot.table.entry.LootContainer;
import com.almostreliable.lootjs.loot.table.entry.LootEntry;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.List;

public class LootContainerWrapper {

    public static LootContainer of(@Nullable Object o) {
        if (o instanceof LootContainer container) {
            return container;
        }

        return ofSingle(o);
    }

    public static LootEntry of(ItemStack in, int count) {
        return LootEntry.ofItemStack(in.kjs$withCount(count));
    }

    public static LootEntry of(ItemStack in, int count, CompoundTag nbt) {
        return LootEntry.ofItemStack(in.kjs$withCount(count).kjs$withNBT(nbt));
    }

    public static LootEntry of(ItemStack in, CompoundTag nbt) {
        return LootEntry.ofItemStack(in.kjs$withNBT(nbt));
    }

    public static CompositeLootEntry ofIngredient(Ingredient ingredient) {
        if (ingredient.isEmpty()) {
            ConsoleJS.SERVER.error("[Loot.ofIngredient()] Invalid ingredient, returning empty group: " + ingredient);
            ConsoleJS.SERVER.error("- Consider using `Loot.empty()` if you want to create an empty loot entry.");
        }

        CompositeLootEntry group = CompositeLootEntry.group();
        for (ItemStack item : ingredient.getItems()) {
            group.getEntries().add(LootEntry.ofItemStack(item));
        }

        return group;
    }

    public static LootEntry tag(String tag) {
        return LootEntry.ofTag(tag, false);
    }

    public static LootEntry tag(String tag, boolean expand) {
        return LootEntry.ofTag(tag, expand);
    }

    public static LootEntry reference(ResourceLocation lootTable) {
        return LootEntry.ofReferece(lootTable);
    }

    public static LootEntry dynamic(ResourceLocation name) {
        return LootEntry.ofDynamic(name);
    }

    public static LootEntry empty() {
        return LootEntry.empty();
    }

    public static CompositeLootEntry alternative(LootContainer... entries) {
        var composite = CompositeLootEntry.alternative();
        if (entries.length != 0) {
            composite.getEntries().addAll(List.of(entries));
        }

        return composite;
    }

    public static CompositeLootEntry sequence(LootContainer... entries) {
        var composite = CompositeLootEntry.sequence();
        if (entries.length != 0) {
            composite.getEntries().addAll(List.of(entries));
        }

        return composite;
    }

    public static CompositeLootEntry group(LootContainer... entries) {
        var composite = CompositeLootEntry.group();
        if (entries.length != 0) {
            composite.getEntries().addAll(List.of(entries));
        }

        return composite;
    }

    public static LootEntry ofSingle(@Nullable Object o) {
        if (o instanceof LootEntry entry) {
            return entry;
        }

        if (o instanceof String str && str.startsWith("#")) {
            String tag = str.substring(0, 1);
            return LootEntry.ofTag(tag, false);
        }

        OutputItem outputItem = OutputItem.of(o);
        ItemStack itemStack = outputItem.item;
        if (itemStack.isEmpty()) {
            ConsoleJS.SERVER.error("[Loot.of()] Invalid item stack, returning empty stack: " + o);
            ConsoleJS.SERVER.error("- Consider using `Loot.empty()` if you want to create an empty loot entry.");
            return LootEntry.empty();
        }

        return LootEntry.ofItemStack(itemStack).withWeight(outputItem.hasChance() ? (int) outputItem.getChance() : 1);
    }
}
