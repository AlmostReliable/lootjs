package com.almostreliable.lootjs.core.entry;

import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.util.DebugInfo;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.jspecify.annotations.Nullable;

public class ItemLootEntry extends AbstractSimpleLootEntry<LootItem> implements SingleLootEntry {

    public ItemLootEntry(LootItem vanillaEntry) {
        super(vanillaEntry);
    }

    public ItemLootEntry(Item item, @Nullable NumberProvider count) {
        super(new LootItem(item.builtInRegistryHolder(),
                LootPoolSingletonContainer.DEFAULT_WEIGHT,
                LootPoolSingletonContainer.DEFAULT_QUALITY,
                EMPTY_CONDITIONS,
                EMPTY_FUNCTIONS));

        if (count != null) {
            getFunctions().setCount(count);
        }
    }

    public ItemLootEntry(Holder<Item> item) {
        super(new LootItem(item,
                LootPoolSingletonContainer.DEFAULT_WEIGHT,
                LootPoolSingletonContainer.DEFAULT_QUALITY,
                EMPTY_CONDITIONS,
                EMPTY_FUNCTIONS));

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

    @Override
    public ItemStack createRandomItem(LootContext context) {
        for (LootItemCondition condition : getConditions()) {
            if (!condition.test(context)) {
                return ItemStack.EMPTY;
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

    @Override
    public ItemLootEntry addCondition(LootItemCondition condition) {
        getConditions().add(condition);
        return this;
    }

    @Override
    public void collectDebugInfo(DebugInfo info) {
        info.add("% Item: " + BuiltInRegistries.ITEM.getKey(getItem()));
        super.collectDebugInfo(info);
    }
}
