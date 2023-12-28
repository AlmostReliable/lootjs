package com.almostreliable.lootjs.loot.modifier;

import com.almostreliable.lootjs.core.LootBucket;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.loot.LootConditionsContainer;
import com.almostreliable.lootjs.loot.LootFunctionsContainer;
import com.almostreliable.lootjs.loot.LootHandlerContainer;
import com.almostreliable.lootjs.loot.modifier.handler.VanillaConditionWrapper;
import com.almostreliable.lootjs.loot.modifier.handler.VanillaFunctionWrapper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GroupedLootHandler implements LootHandler {

    private final LootHandler[] handlers;
    private final NumberProvider repeat;

    public GroupedLootHandler(NumberProvider repeat, Collection<LootHandler> handlers) {
        this.repeat = repeat;
        this.handlers = handlers.toArray(LootHandler[]::new);
    }

    @Override
    public boolean apply(LootContext context, LootBucket loot) {
        int r = repeat.getInt(context);
        for (int i = 0; i < r; i++) {
            for (LootHandler handler : handlers) {
                if (!handler.apply(context, loot)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static class Filtered extends GroupedLootHandler {

        private final ItemFilter lootItemsFilter;

        public Filtered(NumberProvider repeat, Collection<LootHandler> handlers, ItemFilter lootItemsFilter) {
            super(repeat, handlers);
            this.lootItemsFilter = lootItemsFilter;
        }

        @Override
        public boolean apply(LootContext context, LootBucket loot) {
            LootBucket filteredLoot = loot.extract(lootItemsFilter);
            boolean result = super.apply(context, filteredLoot);
            loot.merge(filteredLoot);
            return result;
        }
    }

    public static class Builder implements LootConditionsContainer<Builder>,
                                           LootFunctionsContainer<Builder>,
                                           LootHandlerContainer<Builder> {

        private final List<LootHandler> handlers = new ArrayList<>();
        private NumberProvider repeat = ConstantValue.exactly(1f);
        @Nullable
        private final ItemFilter lootItemsFilter;

        public Builder(ItemFilter lootItemsFilter) {
            this.lootItemsFilter = lootItemsFilter;
        }

        public Builder() {
            this.lootItemsFilter = null;
        }

        public Builder repeat(NumberProvider repeat) {
            this.repeat = repeat;
            return this;
        }

        @Override
        public Builder addCondition(LootItemCondition condition) {
            handlers.add(new VanillaConditionWrapper(condition));
            return this;
        }

        @Override
        public Builder addHandler(LootHandler action) {
            handlers.add(action);
            return this;
        }

        @Override
        public Builder addFunction(LootItemFunction lootItemFunction) {
            return addHandler(new VanillaFunctionWrapper(lootItemFunction));
        }

        public GroupedLootHandler build() {
            if(lootItemsFilter != null) {
                return new Filtered(repeat, handlers, lootItemsFilter);
            }

            return new GroupedLootHandler(repeat, handlers);
        }
    }
}
