package com.almostreliable.lootjs.core.entry;

import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.loot.LootFunctionList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import javax.annotation.Nullable;

public class ItemLootEntry extends AbstractSimpleLootEntry<LootItem> implements SingleLootEntry {

    public ItemLootEntry(LootItem vanillaEntry) {
        super(vanillaEntry);
    }

    public ItemLootEntry(ItemStack itemStack) {
        super(new LootItem(itemStack.getItem().builtInRegistryHolder(),
                LootPoolSingletonContainer.DEFAULT_WEIGHT,
                LootPoolSingletonContainer.DEFAULT_QUALITY,
                LootEntry.EMPTY_CONDITIONS,
                LootEntry.EMPTY_FUNCTIONS));

        functions = new LootFunctionList();
        if (itemStack.getCount() > 1) {
            functions.setCount(ConstantValue.exactly(itemStack.getCount()));
        }

        if (itemStack.getTag() != null) {
            functions.setNbt(itemStack.getTag());
        }
    }

    public ItemLootEntry(Item item, @Nullable NumberProvider count, @Nullable CompoundTag nbt) {
        super(new LootItem(item.builtInRegistryHolder(),
                LootPoolSingletonContainer.DEFAULT_WEIGHT,
                LootPoolSingletonContainer.DEFAULT_QUALITY,
                LootEntry.EMPTY_CONDITIONS,
                LootEntry.EMPTY_FUNCTIONS));

        functions = new LootFunctionList();
        if (count != null) {
            functions.setCount(count);
        }

        if (nbt != null) {
            functions.setNbt(nbt);
        }
    }

    @Override
    public LootPoolEntryType getVanillaType() {
        return LootPoolEntries.ITEM;
    }

    public Item getItem() {
        return vanillaEntry.item.value();
    }

    public void setItem(Item item) {
        if (item == Items.AIR) {
            throw new IllegalStateException("Vanilla Loot Entry cannot be set to AIR, consider using LootEntry.empty()");
        }

        vanillaEntry.item = item.builtInRegistryHolder();
    }

    @Nullable
    @Override
    public ItemStack create(LootContext context) {
        for (LootItemCondition condition : getConditions()) {
            if (!condition.test(context)) {
                return null;
            }
        }

        var item = new ItemStack(getItem());

        for (LootItemFunction function : getFunctions()) {
            item = function.apply(item, context);
        }

        return item;
    }

    public boolean test(ItemFilter filter) {
        return filter.test(new ItemStack(getItem()));
    }
}
