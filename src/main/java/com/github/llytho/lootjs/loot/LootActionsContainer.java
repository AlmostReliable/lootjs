package com.github.llytho.lootjs.loot;

import com.github.llytho.lootjs.core.ILootAction;
import com.github.llytho.lootjs.core.LootEntry;
import com.github.llytho.lootjs.filters.ItemFilter;
import com.github.llytho.lootjs.loot.action.*;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public interface LootActionsContainer<A extends LootActionsContainer<?>> {

    default A addLoot(LootEntry[] itemStacks) {
        return addAction(new AddLootAction(itemStacks));
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

    default A replaceLoot(ItemFilter filter, ItemStack itemStack) {
        return addAction(new ReplaceLootAction(filter, itemStack));
    }

    default A modifyLoot(ItemFilter filter, ModifyLootAction.Callback callback) {
        return addAction(new ModifyLootAction(filter, callback));
    }

    default A triggerExplosion(float radius, boolean destroy, boolean fire) {
        Explosion.BlockInteraction mode = destroy
                                          ? Explosion.BlockInteraction.DESTROY
                                          : Explosion.BlockInteraction.NONE;
        return addAction(new ExplodeAction(radius, mode, fire));
    }

    default A triggerLightningStrike(boolean shouldDamage) {
        return addAction(new LightningStrikeAction(shouldDamage));
    }

    A addAction(ILootAction action);
}
