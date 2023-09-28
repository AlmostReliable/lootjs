package com.almostreliable.lootjs.loot.action;

import com.almostreliable.lootjs.core.ILootAction;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class DropExperienceAction implements ILootAction {
    private final int amount;

    public DropExperienceAction(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        if (origin == null) return true;
        ExperienceOrb.award(context.getLevel(), origin, amount);
        return true;
    }
}
