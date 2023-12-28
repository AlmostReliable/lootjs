package com.almostreliable.lootjs.core.entry;

import com.almostreliable.lootjs.loot.LootConditionList;
import com.almostreliable.lootjs.loot.LootFunctionList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import javax.annotation.Nullable;

public class EmptyLootEntry extends AbstractSimpleLootEntry<EmptyLootItem> implements SingleLootEntry {
    public EmptyLootEntry(EmptyLootItem vanillaEntry) {
        super(vanillaEntry);
    }

    public EmptyLootEntry() {
        super(new EmptyLootItem(
                LootPoolSingletonContainer.DEFAULT_WEIGHT,
                LootPoolSingletonContainer.DEFAULT_QUALITY,
                EMPTY_CONDITIONS,
                EMPTY_FUNCTIONS));
    }

    @Nullable
    @Override
    public ItemStack create(LootContext context) {
        // TODO use composite here
        // TODO also use function composite in the other create methods for item and tag entry
        for (LootItemCondition condition : getConditions()) {
            if (!condition.test(context)) {
                return null;
            }
        }

        return ItemStack.EMPTY;
    }
}
