package com.almostreliable.lootjs.loot.modifier;

import com.almostreliable.lootjs.core.LootBucket;
import com.almostreliable.lootjs.core.LootType;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.core.filters.ResourceLocationFilter;
import com.almostreliable.lootjs.loot.extension.LootContextExtension;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class LootModifier extends GroupedLootAction {

    private final Predicate<LootContext> shouldRun;
    private final String name;

    public LootModifier(Predicate<LootContext> shouldRun, NumberProvider rolls, List<LootItemCondition> conditions, List<LootItemFunction> functions, Collection<LootAction> handlers, String name, ItemFilter containsLootFilter, boolean exact) {
        super(rolls, conditions, functions, handlers, containsLootFilter, exact);
        this.shouldRun = shouldRun;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void run(LootContext context, LootBucket loot) {
        if (!shouldRun.test(context)) {
            return;
        }

        super.apply(context, loot);
    }

    public static class Builder extends GroupedLootAction.Builder {

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

        public LootModifier build() {
            return new LootModifier(shouldRun, rolls, conditions, functions, actions, name, containsLootFilter, exact);
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

    public record EntityFiltered(HolderSet<EntityType<?>> entities) implements Predicate<LootContext> {

        @Override
        public boolean test(LootContext context) {
            Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
            return entity != null && entities.contains(entity.getType().builtInRegistryHolder()) &&
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
