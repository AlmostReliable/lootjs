package com.almostreliable.lootjs.loot.action;

import com.almostreliable.lootjs.core.ILootAction;
import com.almostreliable.lootjs.util.LootContextUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class CustomPlayerAction implements ILootAction {

    private final Consumer<ServerPlayer> action;

    public CustomPlayerAction(Consumer<ServerPlayer> action) {
        Objects.requireNonNull(action);
        this.action = action;
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        ServerPlayer playerOrNull = LootContextUtils.getPlayerOrNull(context);
        if (playerOrNull != null) {
            action.accept(playerOrNull);
        }
        return true;
    }
}
