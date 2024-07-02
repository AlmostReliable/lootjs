package com.almostreliable.lootjs.core.filters;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.neoforge.common.ItemAbility;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public interface ItemFilter {
    ItemFilter NONE = itemStack -> false;
    ItemFilter ANY = itemStack -> true;
    ItemFilter EMPTY = ItemStack::isEmpty;
    ItemFilter ARMOR = itemStack -> itemStack.getItem() instanceof ArmorItem;
    ItemFilter EDIBLE = itemStack -> itemStack.getFoodProperties(null) != null;
    ItemFilter DAMAGEABLE = ItemStack::isDamageableItem;
    ItemFilter DAMAGED = ItemStack::isDamaged;
    ItemFilter ENCHANTED = ItemStack::isEnchanted;
    ItemFilter BLOCK_ITEM = itemStack -> itemStack.getItem() instanceof BlockItem;

    static ItemFilter hasEnchantment(ResourceLocationFilter filter) {
        return hasEnchantment(filter, MinMaxBounds.Ints.ANY);
    }

    static ItemFilter hasEnchantment(ResourceLocationFilter filter, MinMaxBounds.Ints levelBounds) {
        return itemStack -> {
            ItemEnchantments enchantments = itemStack.get(DataComponents.ENCHANTMENTS);
            return hasEnchantmentsInComponent(filter, levelBounds, enchantments);
        };
    }

    static ItemFilter hasStoredEnchantment(ResourceLocationFilter filter) {
        return hasStoredEnchantment(filter, MinMaxBounds.Ints.ANY);
    }

    static ItemFilter hasStoredEnchantment(ResourceLocationFilter filter, MinMaxBounds.Ints levelBounds) {
        return itemStack -> {
            ItemEnchantments enchantments = itemStack.get(DataComponents.STORED_ENCHANTMENTS);
            return hasEnchantmentsInComponent(filter, levelBounds, enchantments);
        };
    }

    private static boolean hasEnchantmentsInComponent(ResourceLocationFilter filter, MinMaxBounds.Ints levelBounds, @Nullable ItemEnchantments enchantments) {
        if (enchantments == null) {
            return false;
        }

        for (var entry : enchantments.entrySet()) {
            boolean matches = entry.getKey().unwrapKey().filter(key -> filter.test(key.location())).isPresent();
            if (matches && levelBounds.matches(entry.getIntValue())) {
                return true;
            }
        }

        return false;
    }

    static ItemFilter tag(String tag) {
        if (tag.startsWith("#")) {
            tag = tag.substring(1);
        }

        return new Tag(TagKey.create(Registries.ITEM, ResourceLocation.parse(tag)));
    }

    static ItemFilter item(ItemStack otherItemStack, boolean checkComponents) {
        if (checkComponents) {
            return itemStack -> ItemStack.isSameItemSameComponents(itemStack, otherItemStack);
        }

        return itemStack -> itemStack.getItem() == otherItemStack.getItem();
    }

    static ItemFilter equipmentSlot(EquipmentSlot slot) {
        return itemStack -> itemStack.getEquipmentSlot() == slot;
    }

    static ItemFilter equipmentSlotGroup(EquipmentSlotGroup slotGroup) {
        return itemStack -> {
            EquipmentSlot equipmentSlot = itemStack.getEquipmentSlot();
            if (equipmentSlot == null) {
                return false;
            }

            return slotGroup.test(equipmentSlot);
        };
    }

    static ItemFilter allOf(ItemFilter... itemFilters) {
        Objects.requireNonNull(itemFilters);
        return switch (itemFilters.length) {
            case 0 -> ANY;
            case 1 -> itemFilters[0];
            case 2 -> itemFilters[0].and(itemFilters[1]);
            default -> itemStack -> {
                for (ItemFilter itemFilter : itemFilters) {
                    if (!itemFilter.test(itemStack)) {
                        return false;
                    }
                }

                return true;
            };
        };
    }

    static ItemFilter not(ItemFilter itemFilter) {
        return itemFilter.negate();
    }

    static ItemFilter anyOf(ItemFilter... itemFilters) {
        Objects.requireNonNull(itemFilters);
        return switch (itemFilters.length) {
            case 0 -> NONE;
            case 1 -> itemFilters[0];
            case 2 -> itemFilters[0].or(itemFilters[1]);
            default -> itemStack -> {
                for (ItemFilter itemFilter : itemFilters) {
                    if (itemFilter.test(itemStack)) {
                        return true;
                    }
                }

                return false;
            };
        };
    }

    static ItemFilter custom(Predicate<ItemStack> predicate) {
        return predicate::test;
    }

    boolean test(ItemStack itemStack);

    default ItemFilter and(ItemFilter other) {
        Objects.requireNonNull(other);
        return (itemStack) -> test(itemStack) && other.test(itemStack);
    }

    default ItemFilter negate() {
        return (itemS) -> !test(itemS);
    }

    default ItemFilter or(ItemFilter other) {
        Objects.requireNonNull(other);
        return (itemStack) -> test(itemStack) || other.test(itemStack);
    }

    static ItemFilter anyToolAction(String... actions) {
        List<ItemAbility> toolActions = Arrays.stream(actions).map(ItemAbility::get).toList();

        if (toolActions.isEmpty()) {
            return itemStack -> true;
        }

        if (toolActions.size() == 1) {
            var action = toolActions.getFirst();
            return itemStack -> itemStack.canPerformAction(action);
        }

        if (toolActions.size() == 2) {
            var action1 = toolActions.get(0);
            var action2 = toolActions.get(1);
            return itemStack -> itemStack.canPerformAction(action1) || itemStack.canPerformAction(action2);
        }

        return itemStack -> {
            for (var action : toolActions) {
                if (itemStack.canPerformAction(action)) {
                    return true;
                }
            }

            return false;
        };
    }

    static ItemFilter toolAction(String... actions) {
        List<ItemAbility> toolActions = Arrays.stream(actions).map(ItemAbility::get).toList();

        if (toolActions.size() == 1) {
            var action = toolActions.getFirst();
            return itemStack -> itemStack.canPerformAction(action);
        }

        if (toolActions.isEmpty()) {
            return itemStack -> true;
        }

        if (toolActions.size() == 2) {
            var action1 = toolActions.get(0);
            var action2 = toolActions.get(1);
            return itemStack -> itemStack.canPerformAction(action1) && itemStack.canPerformAction(action2);
        }

        return itemStack -> {
            for (var action : toolActions) {
                if (!itemStack.canPerformAction(action)) {
                    return false;
                }
            }

            return true;
        };
    }

    record Ingredient(net.minecraft.world.item.crafting.Ingredient ingredient) implements ItemFilter {

        @Override
        public boolean test(ItemStack itemStack) {
            return ingredient.test(itemStack);
        }
    }

    record Tag(TagKey<Item> tag) implements ItemFilter {

        @Override
        public boolean test(ItemStack itemStack) {
            return itemStack.is(tag);
        }
    }
}
