package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.LootJS;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
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
import java.util.function.Consumer;

@SuppressWarnings({ "unused", "UnusedReturnValue" })
public interface LootFunctionsContainer<F> {

    default F enchantRandomly() {
        return addFunction(EnchantRandomlyFunction.randomEnchantment().build());
    }

    default F enchantRandomly(HolderSet<Enchantment> enchantments) {
        var enchantRandomlyFunctionBuilder = EnchantRandomlyFunction.randomEnchantment();
        enchantRandomlyFunctionBuilder.withOneOf(enchantments);
        return addFunction(enchantRandomlyFunctionBuilder.build());
    }

    default F enchantWithLevels(NumberProvider numberProvider) {
        var builder = EnchantWithLevelsFunction.enchantWithLevels(LootJS.lookup(), numberProvider);
        return addFunction(builder.build());
    }

    default F enchant(boolean add, Consumer<SetEnchantmentsFunction.Builder> action) {
        var builder = new SetEnchantmentsFunction.Builder(add);
        action.accept(builder);
        return addFunction(builder.build());
    }

    default F enchant(Consumer<SetEnchantmentsFunction.Builder> action) {
        return enchant(false, action);
    }

    default F applyEnchantmentBonus(Holder<Enchantment> enchantment, NumberProvider count) {
        return addFunction(new EnchantedCountIncreaseFunction.Builder(enchantment, count).build());
    }

    default F applyEnchantmentBonus(NumberProvider count) {
        return addFunction(EnchantedCountIncreaseFunction.lootingMultiplier(LootJS.lookup(), count).build());
    }

    default F applyBinomialDistributionBonus(Holder<Enchantment> enchantment, float probability, int extra) {
        var applyBonusBuilder = ApplyBonusCount.addBonusBinomialDistributionCount(enchantment, probability, extra);
        return addFunction(applyBonusBuilder.build());
    }

    default F applyOreBonus(Holder<Enchantment> enchantment) {
        var applyBonusBuilder = ApplyBonusCount.addOreBonusCount(enchantment);
        return addFunction(applyBonusBuilder.build());
    }

    default F applyBonus(Holder<Enchantment> enchantment, int multiplier) {
        var applyBonusBuilder = ApplyBonusCount.addUniformBonusCount(enchantment, multiplier);
        return addFunction(applyBonusBuilder.build());
    }

    default F simulateExplosionDecay() {
        return addFunction(ApplyExplosionDecay.explosionDecay().build());
    }

    default F smelt() {
        return addFunction(SmeltItemFunction.smelted().build());
    }

    default F damage(NumberProvider numberProvider) {
        return addFunction(SetItemDamageFunction.setDamage(numberProvider).build());
    }

    default F addPotion(Potion potion) {
        return addFunction(SetPotionFunction.setPotion(BuiltInRegistries.POTION.wrapAsHolder(potion)).build());
    }

    default F addAttributes(Consumer<AddAttributesFunction.Builder> action) {
        AddAttributesFunction.Builder builder = new AddAttributesFunction.Builder();
        action.accept(builder);
        return addFunction(builder.build());
    }

    default F limitCount(@Nullable NumberProvider numberProviderMin, @Nullable NumberProvider numberProviderMax) {
        IntRange intRange = new IntRange(numberProviderMin, numberProviderMax);
        return addFunction(LimitCount.limitCount(intRange).build());
    }

    default F setCount(NumberProvider numberProvider) {
        return addFunction(SetItemCountFunction.setCount(numberProvider).build());
    }

    default F addLore(Component... components) {
        SetLoreFunction.Builder builder = SetLoreFunction.setLore();
        for (Component c : components) {
            builder.addLine(c);
        }

        return addFunction(builder.build());
    }

    default F replaceLore(Component... components) {
        SetLoreFunction.Builder builder = SetLoreFunction.setLore();
        for (Component c : components) {
            builder.addLine(c);
        }

        return addFunction(builder.setMode(ListOperation.ReplaceAll.INSTANCE).build());
    }

    default F setName(Component component) {
        return addFunction(SetComponentsFunction.setComponent(DataComponents.CUSTOM_NAME, component).build());
    }

    default F setCustomData(CompoundTag tag) {
        return addFunction(SetCustomDataFunction.setCustomData(tag).build());
    }

    default F toggleTooltips(Map<String, Boolean> toggles) {
        Map<ToggleTooltips.ComponentToggle<?>, Boolean> map = new HashMap<>();
        toggles.forEach((name, flag) -> {
            ResourceLocation id = ResourceLocation.parse(name);
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

        return addFunction(new ToggleTooltips(List.of(), map));
    }

    default F jsonFunction(JsonObject json) {
        var regOps = RegistryOps.create(JsonOps.INSTANCE, LootJS.lookup());
        LootItemFunction function = LootItemFunctions.ROOT_CODEC.parse(regOps, json).getOrThrow();
        return addFunction(function);
    }

    F addFunction(LootItemFunction lootItemFunction);
}
