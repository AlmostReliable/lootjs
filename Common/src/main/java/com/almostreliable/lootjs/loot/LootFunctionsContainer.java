package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.LootJS;
import com.almostreliable.lootjs.core.ILootCondition;
import com.almostreliable.lootjs.filters.ItemFilter;
import com.almostreliable.lootjs.loot.action.LootItemFunctionWrapperAction;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
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

    default F limitCount(@Nullable NumberProvider numberProviderMin, @Nullable NumberProvider numberProviderMax) {
        IntRange intRange = new IntRange(numberProviderMin, numberProviderMax);
        return addFunction(LimitCount.limitCount(intRange));
    }

    default F limitCount(NumberProvider numberProvider) {
        return addFunction(SetItemCountFunction.setCount(numberProvider));
    }

    default F addLore(Component... components) {
        SetLoreFunction.Builder builder = SetLoreFunction.setLore();
        for (Component c : components) {
            builder.addLine(c);
        }
        return addFunction(builder);
    }

    default F replaceLore(Component... components) {
        SetLoreFunction.Builder builder = SetLoreFunction.setLore();
        for (Component c : components) {
            builder.addLine(c);
        }
        return addFunction(builder.setReplace(true));
    }

    default F setName(Component component) {
        return addFunction(SetNameFunction.setName(component));
    }

    default F addNBT(CompoundTag tag) {
        return addFunction(SetNbtFunction.setTag(tag));
    }

    /**
     * For the people who always forget if "NBT" or "Nbt"
     */
    default F addNbt(CompoundTag tag) {
        return addFunction(SetNbtFunction.setTag(tag));
    }

    default F customFunction(JsonObject json) {
        var function = LootJS.FUNCTION_GSON.fromJson(json, LootItemFunction.class);
        return addFunction(function);
    }

    default F functions(ItemFilter filter, Consumer<LootFunctionsContainer<F>> action) {
        // TODO not sure if I like this
        List<LootItemFunction> functions = new ArrayList<>();
        LootFunctionsContainer<F> lfc = new LootFunctionsContainer<>() {
            @Override
            public F addFunction(LootItemFunction lootItemFunction) {
                functions.add(lootItemFunction);
                //noinspection unchecked
                return (F) this;
            }

            @Override
            public F functions(ItemFilter filter, Consumer<LootFunctionsContainer<F>> action) {
                throw new UnsupportedOperationException("Nested `filteredFunctions` are not supported.");
            }
        };
        action.accept(lfc);
        return addFunction(new LootItemFunctionWrapperAction.CompositeLootItemFunction(functions, filter));
    }

    default F addFunction(LootItemFunction.Builder builder) {
        return addFunction(builder.build());
//        return addAction(new LootItemFunctionWrapperAction(builder.build()));
    }

    F addFunction(LootItemFunction lootItemFunction);

//    F addAction(ILootAction action);
}
