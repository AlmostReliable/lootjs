package com.almostreliable.lootjs.loot;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@SuppressWarnings({ "unused", "UnusedReturnValue" })
public interface LootFunctionsContainer<F extends LootFunctionsContainer<?>> {

    default F enchantRandomly() {
        return enchantRandomly(new Enchantment[]{});
    }

    default F enchantRandomly(Enchantment[] enchantments) {
        return addFunction(LootFunction.enchantRandomly(enchantments));
    }

    default F enchantWithLevels(NumberProvider numberProvider) {
        return addFunction(LootFunction.enchantWithLevels(numberProvider));
    }

    default F enchantWithLevels(NumberProvider numberProvider, boolean allowTreasure) {
        return addFunction(LootFunction.enchantWithLevels(numberProvider, allowTreasure));
    }

    default F applyLootingBonus(NumberProvider numberProvider) {
        return addFunction(LootFunction.applyLootingBonus(numberProvider));
    }

    default F applyBinomialDistributionBonus(Enchantment enchantment, float probability, int n) {
        return addFunction(LootFunction.applyBinomialDistributionBonus(enchantment, probability, n));
    }

    default F applyOreBonus(Enchantment enchantment) {
        return addFunction(LootFunction.applyOreBonus(enchantment));
    }

    default F applyBonus(Enchantment enchantment, int multiplier) {
        return addFunction(LootFunction.applyBonus(enchantment, multiplier));
    }

    default F simulateExplosionDecay() {
        return addFunction(LootFunction.simulateExplosionDecay());
    }

    default F smeltLoot() {
        return addFunction(LootFunction.smeltLoot());
    }

    default F damage(NumberProvider numberProvider) {
        return addFunction(LootFunction.damage(numberProvider));
    }

    default F addPotion(Potion potion) {
        return addFunction(LootFunction.addPotion(potion));
    }

    default F addAttributes(Consumer<AddAttributesFunction.Builder> action) {
        return addFunction(LootFunction.addAttributes(action));
    }

    default F limitCount(@Nullable NumberProvider numberProviderMin, @Nullable NumberProvider numberProviderMax) {
        return addFunction(LootFunction.limitCount(numberProviderMin, numberProviderMax));
    }

    default F setCount(NumberProvider numberProvider) {
        return addFunction(LootFunction.setCount(numberProvider));
    }

    default F addLore(Component... components) {
        return addFunction(LootFunction.addLore(components));
    }

    default F replaceLore(Component... components) {
        return addFunction(LootFunction.replaceLore(components));
    }

    default F setName(Component component) {
        return addFunction(LootFunction.setName(component));
    }

    default F setNBT(CompoundTag tag) {
        return addFunction(LootFunction.setNBT(tag));
    }

    default F setNbt(CompoundTag tag) {
        return addFunction(LootFunction.setNbt(tag));
    }

    default F customFunction(JsonObject json) {
        return addFunction(LootFunction.fromJson(json));
    }

    F addFunction(LootItemFunction lootItemFunction);
}
