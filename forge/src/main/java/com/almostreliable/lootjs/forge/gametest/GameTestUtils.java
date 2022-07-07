package com.almostreliable.lootjs.forge.gametest;

import com.almostreliable.lootjs.core.LootJSParamSets;
import com.almostreliable.lootjs.core.ILootContextData;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameTestUtils {
    public static LootContext unknownContext(ServerLevel level, Vec3 origin) {
        LootContext.Builder builder = new LootContext.Builder(level).withParameter(LootContextParams.ORIGIN, origin);
        return builder.create(new LootContextParamSet.Builder().required(LootContextParams.ORIGIN).build());
    }

    public static LootContext chestContext(ServerLevel level, Vec3 origin, @Nullable Player player) {
        LootContext.Builder builder = new LootContext.Builder(level).withParameter(LootContextParams.ORIGIN, origin);
        if (player != null) {
            builder.withParameter(LootContextParams.THIS_ENTITY, player);

        }
        return builder.create(LootContextParamSets.CHEST);
    }

    public static ILootContextData fillExampleLoot(LootContext context) {
        ILootContextData data = context.getParamOrNull(LootJSParamSets.DATA);
        assert data != null;
        data.setGeneratedLoot(Stream
                .of(new ItemStack(Items.DIAMOND),
                        new ItemStack(Items.NETHER_BRICK, 10),
                        new ItemStack(Items.GOLDEN_CHESTPLATE))
                .collect(Collectors.toList()));
        return data;
    }

    public static ILootContextData fillExampleLoot(LootContext context, ItemStack... items) {
        ILootContextData data = context.getParamOrNull(LootJSParamSets.DATA);
        assert data != null;
        data.setGeneratedLoot(Stream.of(items).collect(Collectors.toList()));
        return data;
    }

    public static ILootContextData fillExampleLoot(LootContext context, Item... items) {
        ILootContextData data = context.getParamOrNull(LootJSParamSets.DATA);
        assert data != null;
        data.setGeneratedLoot(Stream.of(items).map(ItemStack::new).collect(Collectors.toList()));
        return data;
    }

    public static <E extends Entity> E simpleEntity(EntityType<E> entityType, ServerLevel level, BlockPos pos) {
        @SuppressWarnings("unchecked")
        E entity = (E) entityType.spawn(level, null, null, pos, MobSpawnType.COMMAND, false, false);
        if (entity == null) {
            throw new RuntimeException("Entity type + '" + entityType.getRegistryName() +
                                       "' cannot be created through 'GameTestUtils::simpleEntity'");
        }
        return entity;
    }

    public static void assertFalse(GameTestHelper helper, boolean condition, String message) {
        if (condition) {
            helper.fail(message);
        }
    }

    public static void assertTrue(GameTestHelper helper, boolean condition, String message) {
        if (!condition) {
            helper.fail(message);
        }
    }

    public static void assertFalse(GameTestHelper helper, boolean condition) {
        assertFalse(helper, condition, "Expected condition to be false");
    }

    public static void assertTrue(GameTestHelper helper, boolean condition) {
        assertTrue(helper, condition, "Expected condition to be true");
    }

    public static void assertEquals(GameTestHelper helper, Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            helper.fail("Expected " + expected + " but got " + actual);
        }
    }

    public static void assertNotEquals(GameTestHelper helper, Object expected, Object actual) {
        if (Objects.equals(expected, actual)) {
            helper.fail("Expected " + expected + " and " + actual + " to be different");
        }
    }

    public static void assertNull(GameTestHelper helper, @Nullable Object actual) {
        if (actual != null) {
            helper.fail("Expected null but got " + actual);
        }
    }

    public static void assertNotNull(GameTestHelper helper, @Nullable Object actual) {
        if (actual == null) {
            helper.fail("Expected not null");
        }
    }
}
