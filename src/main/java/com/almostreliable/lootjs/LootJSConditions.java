package com.almostreliable.lootjs;

import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.loot.condition.*;
import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.criterion.DistancePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Just exist so we have the types registered. But they should not be used for json stuff anyway. So we just return units.
 */
public class LootJSConditions {
    static final DeferredRegister<MapCodec<? extends LootItemCondition>> CONDITIONS = DeferredRegister.create(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            BuildConfig.MOD_ID);

    private static MapCodec<? extends LootItemCondition> create(LootItemCondition unit) {
        return MapCodec.unit(unit);
    }

    public static Holder<MapCodec<? extends LootItemCondition>> MATCH_EQUIP = CONDITIONS.register("match_equip",
            () -> create(new MatchEquipmentSlot(EquipmentSlot.MAINHAND, ItemFilter.NONE)));
    public static Holder<MapCodec<? extends LootItemCondition>> MATCH_ANY_INVENTORY_SLOT = CONDITIONS.register(
            "match_any_inventory_slot",
            () -> create(new MatchAnyInventorySlot(ItemFilter.NONE, false)));
    public static Holder<MapCodec<? extends LootItemCondition>> DISTANCE = CONDITIONS.register("match_distance",
            () -> create(new MatchKillerDistance(DistancePredicate.vertical(MinMaxBounds.Doubles.ANY))));
    public static Holder<MapCodec<? extends LootItemCondition>> ANY_STRUCTURE = CONDITIONS.register("match_structure",
            () -> create(new MatchStructure(HolderSet.direct(), true)));
    public static Holder<MapCodec<? extends LootItemCondition>> BIOME = CONDITIONS.register("match_biome",
            () -> create(new MatchBiome(HolderSet.direct())));
    public static Holder<MapCodec<? extends LootItemCondition>> LIGHT_LEVEL = CONDITIONS.register("light_level",
            () -> create(new IsLightLevel(-1, -1)));
    public static Holder<MapCodec<? extends LootItemCondition>> ANY_DIMENSION = CONDITIONS.register("match_dimension",
            () -> create(new MatchDimension(new Identifier[]{})));
    public static Holder<MapCodec<? extends LootItemCondition>> PARAM = CONDITIONS.register("param",
            () -> create(new CustomParamPredicate<>(LootContextParams.THIS_ENTITY, entity -> false)));
    public static Holder<MapCodec<? extends LootItemCondition>> PLAYER_PARAM = CONDITIONS.register("player_param",
            () -> create(new PlayerParamPredicate(p -> false)));
    public static Holder<MapCodec<? extends LootItemCondition>> MATCH_PLAYER = CONDITIONS.register("match_player",
            () -> create(new MatchPlayer(EntityPredicate.Builder.entity().build())));
}
