package com.almostreliable.lootjs.core.filters;

import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public interface ItemFilter {
    ItemFilter NONE = itemStack -> false;
    ItemFilter ANY = itemStack -> true;
    ItemFilter EMPTY = ItemStack::isEmpty;
    ItemFilter ARMOR = itemStack -> {
        var equipable = itemStack.get(DataComponents.EQUIPPABLE);
        if (equipable == null) return false;
        return equipable.slot().isArmor();
    };
    ItemFilter EDIBLE = itemStack -> itemStack.get(DataComponents.FOOD) != null;
    ItemFilter DAMAGEABLE = ItemStack::isDamageableItem;
    ItemFilter DAMAGED = ItemStack::isDamaged;
    ItemFilter ENCHANTED = ItemStack::isEnchanted;
    ItemFilter BLOCK_ITEM = itemStack -> itemStack.getItem() instanceof BlockItem;

    static ItemFilter hasEnchantment(IdFilter filter) {
        return hasEnchantment(filter, MinMaxBounds.Ints.ANY);
    }

    static ItemFilter hasEnchantment(IdFilter filter, MinMaxBounds.Ints levelBounds) {
        return new ItemFilterImpl.HasEnchantment(filter, levelBounds, DataComponents.ENCHANTMENTS);
    }

    static ItemFilter hasStoredEnchantment(IdFilter filter) {
        return hasStoredEnchantment(filter, MinMaxBounds.Ints.ANY);
    }

    static ItemFilter hasStoredEnchantment(IdFilter filter, MinMaxBounds.Ints levelBounds) {
        return new ItemFilterImpl.HasEnchantment(filter, levelBounds, DataComponents.STORED_ENCHANTMENTS);
    }

    static ItemFilter hasComponent(DataComponentType<?>... types) {
        return new ItemFilterImpl.HasComponent(types);
    }

    static ItemFilter tag(String tag) {
        if (tag.startsWith("#")) {
            tag = tag.substring(1);
        }

        return new ItemFilterImpl.ByTag(TagKey.create(Registries.ITEM, Identifier.parse(tag)));
    }

    static ItemFilter item(ItemStack otherItemStack, boolean checkComponents) {
        return new ItemFilterImpl.ByItem(otherItemStack, checkComponents);
    }

    static ItemFilter equipmentSlot(EquipmentSlot slot) {
        return new ItemFilterImpl.IsEquipmentSlot(slot);
    }

    static ItemFilter equipmentSlotGroup(EquipmentSlotGroup slotGroup) {
        return new ItemFilterImpl.IsEquipmentSlotGroup(slotGroup);
    }

    static ItemFilter anyToolAction(String... actions) {
        var toolActions = Arrays.stream(actions).map(ItemAbility::get).toList();
        var composite = ItemFilterImpl.AnyOfToolAction.compose(toolActions);
        return new ItemFilterImpl.AnyOfToolAction(toolActions, composite);
    }

    static ItemFilter toolAction(String... actions) {
        var toolActions = Arrays.stream(actions).map(ItemAbility::get).toList();
        var composite = ItemFilterImpl.AllOfToolAction.compose(toolActions);
        return new ItemFilterImpl.AllOfToolAction(toolActions, composite);
    }

    static ItemFilter allOf(ItemFilter... itemFilters) {
        Objects.requireNonNull(itemFilters);
        var composite = ItemFilterImpl.AllOf.compose(itemFilters);
        return new ItemFilterImpl.AllOf(itemFilters, composite);
    }

    static ItemFilter not(ItemFilter itemFilter) {
        return itemFilter.negate();
    }

    static ItemFilter anyOf(ItemFilter... itemFilters) {
        Objects.requireNonNull(itemFilters);
        var composite = ItemFilterImpl.AnyOf.compose(itemFilters);
        return new ItemFilterImpl.AnyOf(itemFilters, composite);
    }

    static ItemFilter custom(Predicate<ItemStack> predicate) {
        return new ItemFilterImpl.Custom(predicate, null);
    }

    static ItemFilter custom(Predicate<ItemStack> predicate, String description) {
        return new ItemFilterImpl.Custom(predicate, description);
    }

    boolean test(ItemStack itemStack);

    default ItemFilter and(ItemFilter other) {
        Objects.requireNonNull(other);
        return (itemStack) -> test(itemStack) && other.test(itemStack);
    }

    default ItemFilter negate() {
        return new ItemFilterImpl.Not(this);
    }

    default ItemFilter or(ItemFilter other) {
        Objects.requireNonNull(other);
        return (itemStack) -> test(itemStack) || other.test(itemStack);
    }

}
