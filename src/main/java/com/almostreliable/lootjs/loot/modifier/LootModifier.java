package com.almostreliable.lootjs.loot.modifier;

import com.almostreliable.lootjs.core.LootBucket;
import com.almostreliable.lootjs.core.LootType;
import com.almostreliable.lootjs.core.filters.ResourceLocationFilter;
import com.almostreliable.lootjs.loot.LootConditionsContainer;
import com.almostreliable.lootjs.loot.LootFunctionsContainer;
import com.almostreliable.lootjs.loot.LootHandlerContainer;
import com.almostreliable.lootjs.loot.extension.LootContextExtension;
import com.almostreliable.lootjs.loot.modifier.handler.VanillaConditionWrapper;
import com.almostreliable.lootjs.loot.modifier.handler.VanillaFunctionWrapper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class LootModifier {

    private final Predicate<LootContext> shouldRun;
    private final LootHandler[] handlers;
    private final String name;

    public LootModifier(Predicate<LootContext> shouldRun, Collection<LootHandler> handlers, String name) {
        this.shouldRun = shouldRun;
        this.handlers = handlers.toArray(LootHandler[]::new);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void run(LootContext context, LootBucket loot) {
        if (!shouldRun.test(context)) {
            return;
        }

        for (var handler : handlers) {
            if (!handler.apply(context, loot)) {
                return;
            }
        }
    }

    public static class Builder implements LootConditionsContainer<Builder>,
                                           LootFunctionsContainer<Builder>,
                                           LootHandlerContainer<Builder> {

        private final List<LootHandler> handlers = new ArrayList<>();
        private final Predicate<LootContext> shouldRun;
        private String name;

        public Builder(Predicate<LootContext> shouldRun, String name) {
            this.shouldRun = shouldRun;
            this.name = name;
        }

        public Builder name(String name) {
            this.name = name;
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

        public LootModifier build() {
            return new LootModifier(shouldRun, handlers, name);
        }
    }

    public record TableFiltered(ResourceLocationFilter[] filters) implements Predicate<LootContext> {

        @Override
        public boolean test(LootContext context) {
            for (ResourceLocationFilter filter : filters) {
                if (filter.test(context.getQueriedLootTableId())) {
                    return true;
                }
            }

            return false;
        }
    }

    public record BlockFiltered(Predicate<BlockState> predicate) implements Predicate<LootContext> {

        @Override
        public boolean test(LootContext context) {
            BlockState blockState = context.getParamOrNull(LootContextParams.BLOCK_STATE);
            return blockState != null && predicate.test(blockState) &&
                   LootContextExtension.cast(context).lootjs$isType(LootType.BLOCK);
        }
    }

    public record EntityFiltered(Set<EntityType<?>> entities) implements Predicate<LootContext> {

        @Override
        public boolean test(LootContext context) {
            Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
            return entity != null && entities.contains(entity.getType()) &&
                   LootContextExtension.cast(context).lootjs$isType(LootType.ENTITY);
        }
    }

    public record TypeFiltered(LootType[] types) implements Predicate<LootContext> {

        @Override
        public boolean test(LootContext context) {
            var extension = LootContextExtension.cast(context);
            for (LootType type : types) {
                if (extension.lootjs$isType(type)) {
                    return true;
                }
            }

            return false;
        }
    }
}
