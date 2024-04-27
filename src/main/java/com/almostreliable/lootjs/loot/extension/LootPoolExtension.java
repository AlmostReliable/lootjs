package com.almostreliable.lootjs.loot.extension;

import com.almostreliable.lootjs.loot.LootConditionList;
import com.almostreliable.lootjs.loot.LootEntryList;
import com.almostreliable.lootjs.loot.LootFunctionList;
import com.almostreliable.lootjs.util.DebugInfo;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

public interface LootPoolExtension {

    static LootPoolExtension cast(LootPool pool) {
        return (LootPoolExtension) pool;
    }

    LootPool lootjs$asVanillaPool();

    LootEntryList lootjs$getEntries();

    LootConditionList lootjs$getConditions();

    LootFunctionList lootjs$getFunctions();

    default void lootjs$collectDebugInfo(DebugInfo info) {
        var rollStr = NumberProviders.CODEC
                .encodeStart(JsonOps.INSTANCE, lootjs$asVanillaPool().getRolls())
                .getOrThrow();
        var bonusStr = NumberProviders.CODEC
                .encodeStart(JsonOps.INSTANCE, lootjs$asVanillaPool().getBonusRolls())
                .getOrThrow();
        info.add("% Rolls -> " + rollStr.toString());
        info.add("% Bonus rolls -> " + bonusStr.toString());
        lootjs$getEntries().collectDebugInfo(info);
        lootjs$getConditions().collectDebugInfo(info);
        lootjs$getFunctions().collectDebugInfo(info);
    }
//
//    default void lootjs$collectDebugInfo(DebugInfo info) {
//        var rollStr = NumberProviders.CODEC
//                .encodeStart(JsonOps.INSTANCE, lootjs$asVanillaPool().getRolls())
//                .getOrThrow();
//        var bonusStr = NumberProviders.CODEC
//                .encodeStart(JsonOps.INSTANCE, lootjs$asVanillaPool().getBonusRolls())
//                .getOrThrow();
//        info.add("% Rolls -> " + rollStr.toString());
//        info.add("% Bonus rolls -> " + bonusStr.toString());
//        lootjs$getEntries().collectDebugInfo(info);
//        lootjs$getConditions().collectDebugInfo(info);
//        lootjs$getFunctions().collectDebugInfo(info);
//    }
//
//    default LootPool lootjs$rolls(NumberProvider rolls) {
//        lootjs$asVanillaPool().setRolls(rolls);
//        return lootjs$asVanillaPool();
//    }
//
//    default LootPool lootjs$bonusRolls(NumberProvider bonusRolls) {
//        lootjs$asVanillaPool().setBonusRolls(bonusRolls);
//        return lootjs$asVanillaPool();
//    }
//
//    default LootPool when(Consumer<LootConditionList> onConditions) {
//        onConditions.accept(lootjs$getConditions());
//        return lootjs$asVanillaPool();
//    }
//
//    default LootPool apply(Consumer<LootFunctionList> onModifiers) {
//        onModifiers.accept(lootjs$getFunctions());
//        return lootjs$asVanillaPool();
//    }
//
//    @Override
//    default LootApplier addEntry(LootEntry entry) {
//        return this;
//    }
//
//    @Override
//    default LootApplier transformEntry(UnaryOperator<SimpleLootEntry> onTransform, boolean deepTransform) {
//        return this;
//    }
//
//    @Override
//    default LootApplier removeEntry(Predicate<SimpleLootEntry> onRemove, boolean deepRemove) {
//        return this;
//    }
}
