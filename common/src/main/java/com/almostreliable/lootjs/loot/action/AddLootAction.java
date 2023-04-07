package com.almostreliable.lootjs.loot.action;

import com.almostreliable.lootjs.core.ILootAction;
import com.almostreliable.lootjs.core.LootEntry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;

public class AddLootAction implements ILootAction {

    private final LootEntry[] entries;
    private final AddType type;

    public AddLootAction(LootEntry[] entries) {
        this.entries = entries;
        this.type = AddType.DEFAULT;
    }

    public AddLootAction(LootEntry[] entries, AddType type) {
        this.entries = entries;
        this.type = type;
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        for (LootEntry itemStack : entries) {
            ItemStack item = itemStack.createItem(context);
            if (item != null) {
                loot.add(item);
                if (type == AddType.ALTERNATIVES) return true;
            } else if (type == AddType.SEQUENCE) {
                return true;
            }
        }
        return true;
    }

    public enum AddType {
        DEFAULT,
        SEQUENCE,
        ALTERNATIVES
    }
}
