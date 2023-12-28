package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.core.ItemStackFactory;
import com.almostreliable.lootjs.core.entry.LootEntry;
import com.almostreliable.lootjs.core.entry.SingleLootEntry;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.loot.modifier.CancelableLootAction;
import com.almostreliable.lootjs.loot.modifier.GroupedLootHandler;
import com.almostreliable.lootjs.loot.modifier.LootHandler;
import com.almostreliable.lootjs.loot.modifier.handler.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.function.Consumer;

public interface LootHandlerContainer<A extends LootHandlerContainer<?>> {

    default A addLoot(LootEntry... entries) {
        return addHandler(new AddLootAction(entries));
    }

    default A addAlternativesLoot(LootEntry... entries) {
        return addHandler(new AddLootAction(LootEntry.alternative(entries)));
    }

    default A addSequenceLoot(LootEntry... entries) {
        return addHandler(new AddLootAction(LootEntry.sequence(entries)));
    }

    default A addWeightedLoot(NumberProvider numberProvider, boolean allowDuplicateLoot, SingleLootEntry[] poolEntries) {
        var weightedListBuilder = SimpleWeightedRandomList.<SingleLootEntry>builder();
        for (var poolEntry : poolEntries) {
            weightedListBuilder.add(poolEntry, poolEntry.getWeight());
        }
        return addHandler(new WeightedAddLootAction(numberProvider, weightedListBuilder.build(), allowDuplicateLoot));
    }

    default A addWeightedLoot(NumberProvider numberProvider, SingleLootEntry[] poolEntries) {
        return addWeightedLoot(numberProvider, true, poolEntries);
    }

    default A addWeightedLoot(SingleLootEntry[] poolEntries) {
        return addWeightedLoot(ConstantValue.exactly(1f), true, poolEntries);
    }

    default A removeLoot(ItemFilter filter) {
        return addHandler(new RemoveLootAction(filter));
    }

    default A replaceLoot(ItemFilter filter, ItemStackFactory itemStackFactory) {
        return replaceLoot(filter, itemStackFactory, false);
    }

    default A replaceLoot(ItemFilter filter, ItemStackFactory itemStackFactory, boolean preserveCount) {
        return addHandler(new ReplaceLootAction(filter, itemStackFactory, preserveCount));
    }

    default A modifyLoot(ItemFilter filter, ModifyLootAction.Callback callback) {
        return addHandler(new ModifyLootAction(filter, callback));
    }

    default A triggerExplosion(float radius, boolean destroy, boolean fire) {
        Explosion.BlockInteraction mode = destroy
                                          ? Explosion.BlockInteraction.DESTROY
                                          : Explosion.BlockInteraction.KEEP;
        return addHandler(new ExplodeAction(radius, mode, fire));
    }

    default A triggerExplosion(float radius, Explosion.BlockInteraction mode, boolean fire) {
        return addHandler(new ExplodeAction(radius, mode, fire));
    }

    default A triggerLightningStrike(boolean shouldDamage) {
        return addHandler(new LightningStrikeAction(shouldDamage));
    }

    default A dropExperience(int amount) {
        return addHandler(new DropExperienceAction(amount));
    }

    default A customAction(CancelableLootAction action) {
        return addHandler(CancelableLootAction.asHandler(action));
    }

    default A playerAction(Consumer<ServerPlayer> action) {
        return addHandler(new CustomPlayerAction(action));
    }

    default A group(ItemFilter filter, Consumer<GroupedLootHandler.Builder> callback) {
        GroupedLootHandler.Builder builder = new GroupedLootHandler.Builder(filter);
        callback.accept(builder);
        return addHandler(builder.build());
    }

    default A group(Consumer<GroupedLootHandler.Builder> callback) {
        GroupedLootHandler.Builder builder = new GroupedLootHandler.Builder();
        callback.accept(builder);
        return addHandler(builder.build());
    }


    default A matchLoot(ItemFilter filter) {
        return matchLoot(filter, false);
    }

    default A matchLoot(ItemFilter filter, boolean exact) {
        return addHandler(new ContainsLootCheck(filter, exact));
    }

    A addHandler(LootHandler action);
}
