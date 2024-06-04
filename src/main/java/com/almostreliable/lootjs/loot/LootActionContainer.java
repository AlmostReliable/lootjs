package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.core.ItemStackFactory;
import com.almostreliable.lootjs.core.entry.LootEntry;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.loot.modifier.GroupedLootAction;
import com.almostreliable.lootjs.loot.modifier.LootAction;
import com.almostreliable.lootjs.loot.modifier.handler.*;
import com.almostreliable.lootjs.loot.table.MutableLootPool;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.storage.loot.LootPool;

import java.util.function.Consumer;

public interface LootActionContainer<A extends LootActionContainer<?>> {

    default A addLoot(LootEntry... entries) {
        return addAction(new AddLootAction(entries));
    }

    default A addAlternativesLoot(LootEntry... entries) {
        return addAction(new AddLootAction(LootEntry.alternative(entries)));
    }

    default A addSequenceLoot(LootEntry... entries) {
        return addAction(new AddLootAction(LootEntry.sequence(entries)));
    }

    default A removeLoot(ItemFilter filter) {
        return addAction(new RemoveLootAction(filter));
    }

    default A replaceLoot(ItemFilter filter, ItemStackFactory itemStackFactory) {
        return replaceLoot(filter, itemStackFactory, false);
    }

    default A replaceLoot(ItemFilter filter, ItemStackFactory itemStackFactory, boolean preserveCount) {
        return addAction(new ReplaceLootAction(filter, itemStackFactory, preserveCount));
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

    default A customAction(LootAction action) {
        return addAction(action);
    }

    default A playerAction(Consumer<ServerPlayer> action) {
        return addAction(new CustomPlayerAction(action));
    }

    default A group(ItemFilter filter, Consumer<GroupedLootAction.Builder> onCreateGroup) {
        GroupedLootAction.Builder builder = new GroupedLootAction.Builder(filter);
        onCreateGroup.accept(builder);
        return addAction(builder.build());
    }

    default A group(Consumer<GroupedLootAction.Builder> onCreateGroup) {
        GroupedLootAction.Builder builder = new GroupedLootAction.Builder();
        onCreateGroup.accept(builder);
        return addAction(builder.build());
    }

    default A pool(Consumer<MutableLootPool> onCreatePool) {
        MutableLootPool pool = new MutableLootPool(LootPool.lootPool().build());
        onCreatePool.accept(pool);
        return addAction(new LootPoolAction(pool.getVanillaPool()));
    }

    A addAction(LootAction action);
}
