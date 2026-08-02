package com.almostreliable.lootjs.core.filters;

import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.neoforge.common.ItemAbility;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class ItemFilterImpl {
    public record HasEnchantment(IdFilter filter, MinMaxBounds.Ints levelBounds,
                                 DataComponentType<ItemEnchantments> type)
            implements ItemFilter {
        @Override
        public boolean test(ItemStack itemStack) {
            var enchantments = itemStack.get(type);
            if (enchantments == null) {
                return false;
            }

            return hasEnchantmentsInComponent(enchantments);
        }

        public boolean hasEnchantmentsInComponent(ItemEnchantments enchantments) {
            for (var entry : enchantments.entrySet()) {
                boolean matches = entry.getKey().unwrapKey().filter(key -> filter.test(key.identifier())).isPresent();
                if (matches && levelBounds.matches(entry.getIntValue())) {
                    return true;
                }
            }

            return false;
        }
    }

    public record HasComponent(DataComponentType<?>[] types) implements ItemFilter {

        @Override
        public boolean test(ItemStack itemStack) {
            for (var type : types) {
                if (!itemStack.has(type)) {
                    return false;
                }
            }

            return true;
        }
    }

    public record IsEquipmentSlot(EquipmentSlot equipmentSlot) implements ItemFilter {

        @Override
        public boolean test(ItemStack itemStack) {
            return itemStack.getEquipmentSlot() == equipmentSlot;
        }
    }

    public record IsEquipmentSlotGroup(EquipmentSlotGroup equipmentSlotGroup) implements ItemFilter {

        @Override
        public boolean test(ItemStack itemStack) {
            EquipmentSlot equipmentSlot = itemStack.getEquipmentSlot();
            if (equipmentSlot == null) {
                return false;
            }

            return equipmentSlotGroup.test(equipmentSlot);
        }
    }

    public record ByItem(ItemStack filter, boolean checkComponents) implements ItemFilter {

        @Override
        public boolean test(ItemStack itemStack) {
            if (checkComponents) {
                return ItemStack.isSameItemSameComponents(itemStack, filter);
            }

            return itemStack.getItem() == filter.getItem();
        }
    }

    public record ByIngredient(Ingredient ingredient) implements ItemFilter {

        @Override
        public boolean test(ItemStack itemStack) {
            return ingredient.test(itemStack);
        }
    }

    public record ByTag(TagKey<Item> tag) implements ItemFilter {

        @Override
        public boolean test(ItemStack itemStack) {
            return itemStack.is(tag);
        }
    }

    public record AnyOfToolAction(List<ItemAbility> toolActions, Predicate<ItemStack> composite) implements ItemFilter {

        public static Predicate<ItemStack> compose(List<ItemAbility> toolActions) {
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

        @Override
        public boolean test(ItemStack itemStack) {
            return composite.test(itemStack);
        }

    }

    public record AllOfToolAction(List<ItemAbility> toolActions, Predicate<ItemStack> composite) implements ItemFilter {

        public static Predicate<ItemStack> compose(List<ItemAbility> toolActions) {
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

        @Override
        public boolean test(ItemStack itemStack) {
            return composite.test(itemStack);
        }

    }

    public record Custom(Predicate<ItemStack> predicate, @Nullable String description) implements ItemFilter {

        @Override
        public boolean test(ItemStack itemStack) {
            return predicate.test(itemStack);
        }

    }

    public record Not(ItemFilter itemFilter) implements ItemFilter {

        @Override
        public boolean test(ItemStack itemStack) {
            return !itemFilter.test(itemStack);
        }
    }

    public record AllOf(ItemFilter[] itemFilters, ItemFilter composite) implements ItemFilter {

        public static ItemFilter compose(ItemFilter[] itemFilters) {
            return switch (itemFilters.length) {
                case 0 -> ItemFilter.ANY;
                case 1 -> itemFilters[0];
                case 2 -> {
                    var itemFilter1 = itemFilters[0];
                    var itemFilter2 = itemFilters[1];
                    yield itemStack -> itemFilter1.test(itemStack) && itemFilter2.test(itemStack);
                }
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

        @Override
        public boolean test(ItemStack itemStack) {
            return composite.test(itemStack);
        }

        public ItemFilter[] itemFilters() {
            return itemFilters.clone();
        }
    }

    public record AnyOf(ItemFilter[] itemFilters, ItemFilter composite) implements ItemFilter {

        public static ItemFilter compose(ItemFilter[] itemFilters) {
            return switch (itemFilters.length) {
                case 0 -> ItemFilter.NONE;
                case 1 -> itemFilters[0];
                case 2 -> {
                    var itemFilter1 = itemFilters[0];
                    var itemFilter2 = itemFilters[1];
                    yield itemStack -> itemFilter1.test(itemStack) || itemFilter2.test(itemStack);
                }
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

        @Override
        public boolean test(ItemStack itemStack) {
            return composite.test(itemStack);
        }

        public ItemFilter[] itemFilters() {
            return itemFilters.clone();
        }
    }

}














