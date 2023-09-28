package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.core.ILootAction;
import com.almostreliable.lootjs.filters.ItemFilter;
import com.almostreliable.lootjs.loot.action.*;
import com.almostreliable.lootjs.loot.table.entry.LootEntry;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public interface LootActionsContainer<A extends LootActionsContainer<?>> {

    default A addLoot(LootEntry... entries) {
        return addAction(new AddLootAction(entries));
    }

    default A addAlternativesLoot(LootEntry... entries) {
        return addAction(new AddLootAction(entries, AddLootAction.AddType.ALTERNATIVES));
    }

    default A addSequenceLoot(LootEntry... entries) {
        return addAction(new AddLootAction(entries, AddLootAction.AddType.SEQUENCE));
    }

    default A addWeightedLoot(NumberProvider numberProvider, boolean allowDuplicateLoot, LootEntry[] poolEntries) {
        var weightedListBuilder = SimpleWeightedRandomList.<LootEntry>builder();
        for (var poolEntry : poolEntries) {
            weightedListBuilder.add(poolEntry, poolEntry.getWeight());
        }
        return addAction(new WeightedAddLootAction(numberProvider, weightedListBuilder.build(), allowDuplicateLoot));
    }

    default A addWeightedLoot(NumberProvider numberProvider, LootEntry[] poolEntries) {
        return addWeightedLoot(numberProvider, true, poolEntries);
    }

    default A addWeightedLoot(LootEntry[] poolEntries) {
        return addWeightedLoot(ConstantValue.exactly(1f), true, poolEntries);
    }

    default A removeLoot(ItemFilter filter) {
        return addAction(new RemoveLootAction(filter));
    }

    default A replaceLoot(ItemFilter filter, LootEntry lootEntry) {
        return replaceLoot(filter, lootEntry, false);
    }

    default A replaceLoot(ItemFilter filter, LootEntry lootEntry, boolean preserveCount) {
        return addAction(new ReplaceLootAction(filter, lootEntry, preserveCount));
    }

    default A modifyLoot(ItemFilter filter, ModifyLootAction.Callback callback) {
        return addAction(new ModifyLootAction(filter, callback));
    }

    default A triggerExplosion(float radius, boolean destroy, boolean fire) {
        Explosion.BlockInteraction mode = destroy
                                          ? Explosion.BlockInteraction.DESTROY
                                          : Explosion.BlockInteraction.KEEP;
        return addAction(new ExplodeAction(radius, mode, fire));
    }

    default A triggerExplosion(float radius, Explosion.BlockInteraction mode, boolean fire) {
        return addAction(new ExplodeAction(radius, mode, fire));
    }

    default A triggerLightningStrike(boolean shouldDamage) {
        return addAction(new LightningStrikeAction(shouldDamage));
    }

    default A dropExperience(int amount) {
        return addAction(new DropExperienceAction(amount));
    }

    A addAction(ILootAction action);
}
