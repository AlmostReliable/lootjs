package com.almostreliable.lootjs.core;

import com.almostreliable.lootjs.loot.LootFunctionsContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class LootEntry implements LootFunctionsContainer<LootEntry>, Function<LootContext, ItemStack> {

    private final ItemStack itemStack;
    private final List<LootItemFunction> postModifications = new ArrayList<>();
    private int weight = 1;

    public LootEntry(Item item) {
        this.itemStack = new ItemStack(item);
    }

    public LootEntry(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public ItemStack apply(LootContext context) {
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack result = itemStack.copy();
        for (LootItemFunction lootItemFunction : postModifications) {
            result = lootItemFunction.apply(result, context);
        }
        return result;
    }

    @Override
    public LootEntry addFunction(LootItemFunction lootItemFunction) {
        postModifications.add(lootItemFunction);
        return this;
    }

    public boolean hasWeight() {
        return weight >= 1;
    }

    public int getWeight() {
        return weight;
    }

    public LootEntry withWeight(int weight) {
        this.weight = Math.max(1, weight);
        return this;
    }

    public LootEntry withChance(int chance) {
        return withWeight(chance);
    }
}
