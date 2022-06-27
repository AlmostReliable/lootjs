package com.almostreliable.lootjs.predicate;

import com.google.gson.JsonElement;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class CustomItemPredicate extends ItemPredicate {

    private final Predicate<ItemStack> predicate;

    public CustomItemPredicate(Predicate<ItemStack> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean matches(ItemStack item) {
        return predicate.test(item);
    }

    @Override
    public JsonElement serializeToJson() {
        throw new UnsupportedOperationException("Not supported for custom predicates from LootJS mod");
    }
}
