package com.github.llytho.lootjs.loot.condition;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootContextData;
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
    public boolean test(LootContext context) {
        ILootContextData data = context.getParamOrNull(Constants.DATA);
        if (data == null) {
            return false;
        }

        return exact ? matchExact(data.getGeneratedLoot()) : match(data.getGeneratedLoot());
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
