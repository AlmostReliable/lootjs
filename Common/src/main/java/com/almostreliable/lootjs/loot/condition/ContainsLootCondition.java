package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.core.LootJSParamSets;
import com.almostreliable.lootjs.core.ILootContextData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;
import java.util.function.Predicate;

public class ContainsLootCondition implements IExtendedLootCondition {
    private final Predicate<ItemStack> predicate;
    private final boolean exact;

    public ContainsLootCondition(Predicate<ItemStack> predicate, boolean exact) {
        this.predicate = predicate;
        this.exact = exact;
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        return exact ? matchExact(loot) : match(loot);
    }

    @Override
    public boolean test(LootContext context) {
        ILootContextData data = context.getParamOrNull(LootJSParamSets.DATA);
        if (data == null) {
            return false;
        }
        return applyLootHandler(context, data.getGeneratedLoot());
    }

    private boolean match(List<ItemStack> generatedLoot) {
        for (ItemStack itemStack : generatedLoot) {
            if (predicate.test(itemStack)) {
                return true;
            }
        }

        return false;
    }

    private boolean matchExact(List<ItemStack> generatedLoot) {
        for (ItemStack itemStack : generatedLoot) {
            if (!predicate.test(itemStack)) {
                return false;
            }
        }

        return true;
    }
}
