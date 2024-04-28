package com.almostreliable.lootjs.loot;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class LootFunction {

    public static LootItemFunction enchantRandomly() {
        return enchantRandomly(new Enchantment[]{});
    }

    public static LootItemFunction enchantRandomly(Enchantment[] enchantments) {
        EnchantRandomlyFunction.Builder enchantRandomlyFunctionBuilder = EnchantRandomlyFunction.randomEnchantment();
        for (Enchantment enchantment : enchantments) {
            enchantRandomlyFunctionBuilder.withEnchantment(enchantment);
        }

        return enchantRandomlyFunctionBuilder.build();
    }

    public static LootItemFunction enchantWithLevels(NumberProvider numberProvider) {
        return enchantWithLevels(numberProvider, true);
    }

    public static LootItemFunction enchantWithLevels(NumberProvider numberProvider, boolean allowTreasure) {
        EnchantWithLevelsFunction.Builder builder = EnchantWithLevelsFunction.enchantWithLevels(numberProvider);
        if (allowTreasure) builder.allowTreasure();
        return builder.build();
    }

    public static LootItemFunction applyLootingBonus(NumberProvider numberProvider) {
        LootingEnchantFunction.Builder lootingEnchantBuilder = LootingEnchantFunction.lootingMultiplier(numberProvider);
        return lootingEnchantBuilder.build();
    }

    public static LootItemFunction applyBinomialDistributionBonus(Enchantment enchantment, float probability, int extra) {
        LootItemConditionalFunction.Builder<?> applyBonusBuilder = ApplyBonusCount
                .addBonusBinomialDistributionCount(enchantment, probability, extra);
        return applyBonusBuilder.build();
    }

    public static LootItemFunction applyOreBonus(Enchantment enchantment) {
        LootItemConditionalFunction.Builder<?> applyBonusBuilder = ApplyBonusCount.addOreBonusCount(enchantment);
        return applyBonusBuilder.build();
    }

    public static LootItemFunction applyBonus(Enchantment enchantment, int multiplier) {
        LootItemConditionalFunction.Builder<?> applyBonusBuilder = ApplyBonusCount.addUniformBonusCount(enchantment,
                multiplier);
        return applyBonusBuilder.build();
    }

    public static LootItemFunction simulateExplosionDecay() {
        return ApplyExplosionDecay.explosionDecay().build();
    }

    public static LootItemFunction smelt() {
        return SmeltItemFunction.smelted().build();
    }

    public static LootItemFunction damage(NumberProvider numberProvider) {
        return SetItemDamageFunction.setDamage(numberProvider).build();
    }

    public static LootItemFunction addPotion(Potion potion) {
        Objects.requireNonNull(potion);
        return SetPotionFunction.setPotion(BuiltInRegistries.POTION.wrapAsHolder(potion)).build();
    }

    public static LootItemFunction addAttributes(Consumer<AddAttributesFunction.Builder> action) {
        AddAttributesFunction.Builder builder = new AddAttributesFunction.Builder();
        action.accept(builder);
        return builder.build();
    }

    public static LootItemFunction limitCount(@Nullable NumberProvider numberProviderMin, @Nullable NumberProvider numberProviderMax) {
        IntRange intRange = new IntRange(numberProviderMin, numberProviderMax);
        return LimitCount.limitCount(intRange).build();
    }

    public static LootItemFunction setCount(NumberProvider numberProvider) {
        return SetItemCountFunction.setCount(numberProvider).build();
    }

    public static LootItemFunction addLore(Component... components) {
        SetLoreFunction.Builder builder = SetLoreFunction.setLore();
        for (Component c : components) {
            builder.addLine(c);
        }
        return builder.build();
    }

    public static LootItemFunction replaceLore(Component... components) {
        SetLoreFunction.Builder builder = SetLoreFunction.setLore();
        for (Component c : components) {
            builder.addLine(c);
        }

        return builder.setMode(ListOperation.ReplaceAll.INSTANCE).build();
    }

    public static LootItemFunction setName(Component component) {
        return SetComponentsFunction.setComponent(DataComponents.CUSTOM_NAME, component).build();
    }

    public static LootItemFunction setNbt(CompoundTag tag) {
        return SetCustomDataFunction.setCustomData(tag).build();
    }

    public static LootItemFunction toggleTooltips(Map<String, Boolean> toggles) {
        Map<ToggleTooltips.ComponentToggle<?>, Boolean> map = new HashMap<>();
        toggles.forEach((name, flag) -> {
            ResourceLocation id = new ResourceLocation(name);
            DataComponentType<?> type = BuiltInRegistries.DATA_COMPONENT_TYPE.get(id);
            if (type == null) {
                throw new IllegalArgumentException("Component type not found: " + name);
            }

            ToggleTooltips.ComponentToggle<?> toggle = ToggleTooltips.TOGGLES.get(type);
            if (toggle == null) {
                throw new IllegalArgumentException("Can't toggle tooltip visiblity for: " + name);
            }

            map.put(toggle, flag);
        });

        return new ToggleTooltips(List.of(), map);
    }

    public static LootItemFunction fromJson(JsonObject json) {
        return LootItemFunctions.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow().value();
    }
}
