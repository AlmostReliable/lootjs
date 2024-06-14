package com.almostreliable.lootjs.loot;

import com.google.gson.JsonObject;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings({ "unused", "UnusedReturnValue" })
public interface LootFunctionsContainer<F extends LootFunctionsContainer<?>> {

    default F enchantRandomly() {
        return addFunction(LootFunction.enchantRandomly());
    }

    default F enchantRandomly(List<Holder<Enchantment>> enchantments) {
        return addFunction(LootFunction.enchantRandomly(enchantments));
    }

    default F enchantWithLevels(NumberProvider numberProvider) {
        return addFunction(LootFunction.enchantWithLevels(numberProvider));
    }

    default F enchant(boolean add, Consumer<SetEnchantmentsFunction.Builder> action) {
        return addFunction(LootFunction.enchant(add, action));
    }

    default F enchant(Consumer<SetEnchantmentsFunction.Builder> action) {
        return enchant(false, action);
    }

    default F applyEnchantmentBonus(Holder<Enchantment> enchantment, NumberProvider count) {
        return addFunction(LootFunction.applyEnchantmentBonus(enchantment, count));
    }

    default F applyEnchantmentBonus(NumberProvider count) {
        return addFunction(LootFunction.applyEnchantmentBonus(count));
    }

    default F applyBinomialDistributionBonus(Holder<Enchantment> enchantment, float probability, int extra) {
        return addFunction(LootFunction.applyBinomialDistributionBonus(enchantment, probability, extra));
    }

    default F applyOreBonus(Holder<Enchantment> enchantment) {
        return addFunction(LootFunction.applyOreBonus(enchantment));
    }

    default F applyBonus(Holder<Enchantment> enchantment, int multiplier) {
        return addFunction(LootFunction.applyBonus(enchantment, multiplier));
    }

    default F simulateExplosionDecay() {
        return addFunction(LootFunction.simulateExplosionDecay());
    }

    default F smelt() {
        return addFunction(LootFunction.smelt());
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

    default F setNbt(CompoundTag tag) {
        return addFunction(LootFunction.setNbt(tag));
    }

    default F toggleTooltips(Map<String, Boolean> toggles) {
        return addFunction(LootFunction.toggleTooltips(toggles));
    }

    default F jsonFunction(JsonObject json) {
        return addFunction(LootFunction.fromJson(json));
    }

    F addFunction(LootItemFunction lootItemFunction);
}
