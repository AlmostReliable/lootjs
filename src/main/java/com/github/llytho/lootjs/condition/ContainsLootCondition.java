package com.github.llytho.lootjs.condition;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ICondition;
import com.github.llytho.lootjs.core.ILootContextData;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class ContainsLootCondition extends ValueCondition<Predicate<ItemStack>, List<ItemStack>> {

    public ContainsLootCondition(Predicate<ItemStack>[] items, ICondition<Predicate<ItemStack>, List<ItemStack>> func) {
        super(items, func);
    }

    @Override
    protected boolean match(List<ItemStack> pLoot, Predicate<ItemStack> pPredicate) {
        for (ItemStack itemStack : pLoot) {
            if (pPredicate.test(itemStack)) {
                return true;
            }
        }

        return false;
    }

    @Nullable
    @Override
    protected List<ItemStack> getValue(LootContext pContext) {
        ILootContextData data = pContext.getParamOrNull(Constants.DATA);
        if (data == null) {
            return null;
        }

        return data.getGeneratedLoot();
    }
}
