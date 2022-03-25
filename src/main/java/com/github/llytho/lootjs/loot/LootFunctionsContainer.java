package com.github.llytho.lootjs.loot;

import com.github.llytho.lootjs.core.ILootAction;
import com.github.llytho.lootjs.loot.action.LootItemFunctionWrapperAction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public interface LootFunctionsContainer<F extends LootFunctionsContainer<?>> {

    default F enchantRandomly() {
        return enchantRandomly(new Enchantment[]{});
    }

    default F enchantRandomly(Enchantment[] enchantments) {
        EnchantRandomlyFunction.Builder builder = EnchantRandomlyFunction.randomEnchantment();
        for (Enchantment enchantment : enchantments) {
            builder.withEnchantment(enchantment);
        }
        LootItemFunction enchantRandomlyFunction = builder.build();
        return wrap(enchantRandomlyFunction);
    }

    default F enchantWithLevels(NumberProvider numberProvider) {
        return enchantWithLevels(numberProvider, true);
    }

    default F enchantWithLevels(NumberProvider numberProvider, boolean allowTreasure) {
        EnchantWithLevelsFunction.Builder builder = EnchantWithLevelsFunction.enchantWithLevels(numberProvider);
        if (allowTreasure) builder.allowTreasure();
        LootItemFunction enchantWithLevelsFunction = builder.build();
        return wrap(enchantWithLevelsFunction);
    }

    default F applyLootingBonus(NumberProvider numberProvider) {
        LootItemFunction lootingEnchantFunction = LootingEnchantFunction.lootingMultiplier(numberProvider).build();
        return wrap(lootingEnchantFunction);
    }

    default F applyBinomialDistributionBonus(Enchantment enchantment, float probability, int n) {
        LootItemFunction applyBonusFunction = ApplyBonusCount
                .addBonusBinomialDistributionCount(enchantment, probability, n)
                .build();
        return wrap(applyBonusFunction);
    }

    default F applyOreBonus(Enchantment enchantment) {
        LootItemFunction applyBonusFunction = ApplyBonusCount.addOreBonusCount(enchantment).build();
        return wrap(applyBonusFunction);
    }

    default F applyBonus(Enchantment enchantment, int multiplier) {
        LootItemFunction applyBonusFunction = ApplyBonusCount.addUniformBonusCount(enchantment, multiplier).build();
        return wrap(applyBonusFunction);
    }

    default F simulateExplosionDecay() {
        LootItemFunction explosionDecayFunction = ApplyExplosionDecay.explosionDecay().build();
        return wrap(explosionDecayFunction);
    }

    default F smeltLoot() {
        LootItemFunction smeltFunction = SmeltItemFunction.smelted().build();
        return wrap(smeltFunction);
    }

    default F damage(NumberProvider numberProvider) {
        LootItemFunction damageFunction = SetItemDamageFunction.setDamage(numberProvider).build();
        return wrap(damageFunction, ItemStack::isDamageableItem);
    }

    default F addPotion(Potion potion) {
        Objects.requireNonNull(potion);
        LootItemFunction potionFunction = SetPotionFunction.setPotion(potion).build();
        return wrap(potionFunction, itemStack -> itemStack.getItem() instanceof PotionItem);
    }

    default F addAttributes(Consumer<AddAttributesFunction.Builder> action) {
        AddAttributesFunction.Builder builder = new AddAttributesFunction.Builder();
        action.accept(builder);
        return wrap(builder.build());
    }

    default F wrap(LootItemFunction lootItemFunction) {
        return addAction(new LootItemFunctionWrapperAction(lootItemFunction));
    }

    default F wrap(LootItemFunction lootItemFunction, Predicate<ItemStack> preFilter) {
        return addAction(new LootItemFunctionWrapperAction(lootItemFunction, preFilter));
    }

    F addAction(ILootAction action);
}
