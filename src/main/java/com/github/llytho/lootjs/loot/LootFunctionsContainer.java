package com.github.llytho.lootjs.loot;

import com.github.llytho.lootjs.core.ILootAction;
import com.github.llytho.lootjs.loot.action.LootItemFunctionWrapperAction;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.Objects;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public interface LootFunctionsContainer<F extends LootFunctionsContainer<?>> {

    default F enchantRandomly() {
        return enchantRandomly(new Enchantment[]{});
    }

    default F enchantRandomly(Enchantment[] enchantments) {
        EnchantRandomlyFunction.Builder enchantRandomlyFunctionBuilder = EnchantRandomlyFunction.randomEnchantment();
        for (Enchantment enchantment : enchantments) {
            enchantRandomlyFunctionBuilder.withEnchantment(enchantment);
        }
        return addFunction(enchantRandomlyFunctionBuilder);
    }

    default F enchantWithLevels(NumberProvider numberProvider) {
        return enchantWithLevels(numberProvider, true);
    }

    default F enchantWithLevels(NumberProvider numberProvider, boolean allowTreasure) {
        EnchantWithLevelsFunction.Builder ewlBuilder = EnchantWithLevelsFunction.enchantWithLevels(numberProvider);
        if (allowTreasure) ewlBuilder.allowTreasure();
        return addFunction(ewlBuilder);
    }

    default F applyLootingBonus(NumberProvider numberProvider) {
        LootingEnchantFunction.Builder lootingEnchantBuilder = LootingEnchantFunction.lootingMultiplier(numberProvider);
        return addFunction(lootingEnchantBuilder);
    }

    default F applyBinomialDistributionBonus(Enchantment enchantment, float probability, int n) {
        LootItemConditionalFunction.Builder<?> applyBonusBuilder = ApplyBonusCount
                .addBonusBinomialDistributionCount(enchantment, probability, n);
        return addFunction(applyBonusBuilder);
    }

    default F applyOreBonus(Enchantment enchantment) {
        LootItemConditionalFunction.Builder<?> applyBonusBuilder = ApplyBonusCount.addOreBonusCount(enchantment);
        return addFunction(applyBonusBuilder);
    }

    default F applyBonus(Enchantment enchantment, int multiplier) {
        LootItemConditionalFunction.Builder<?> applyBonusBuilder = ApplyBonusCount.addUniformBonusCount(enchantment,
                multiplier);
        return addFunction(applyBonusBuilder);
    }

    default F simulateExplosionDecay() {
        return addFunction(ApplyExplosionDecay.explosionDecay());
    }

    default F smeltLoot() {
        return addFunction(SmeltItemFunction.smelted());
    }

    default F damage(NumberProvider numberProvider) {
        return addFunction(SetItemDamageFunction.setDamage(numberProvider));
    }

    default F addPotion(Potion potion) {
        Objects.requireNonNull(potion);
        return addFunction(SetPotionFunction.setPotion(potion));
    }

    default F addAttributes(Consumer<AddAttributesFunction.Builder> action) {
        AddAttributesFunction.Builder builder = new AddAttributesFunction.Builder();
        action.accept(builder);
        return addFunction(builder);
    }

    default F addFunction(LootItemFunction.Builder builder) {
        return addAction(new LootItemFunctionWrapperAction(builder.build()));
    }

    F addAction(ILootAction action);
}
