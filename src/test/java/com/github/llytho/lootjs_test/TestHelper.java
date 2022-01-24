package com.github.llytho.lootjs_test;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.DebugStack;
import com.github.llytho.lootjs.core.ILootContextData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamset;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamsets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.util.DamageSource;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestHelper {
    public final ServerWorld level;
    public final ServerPlayer player;
    public final DebugStack debugStack;
    private int sum;
    private int failed;

    public TestHelper(DebugStack debugStack, ServerWorld level, ServerPlayer player) {
        this.debugStack = debugStack;
        this.level = level;
        this.player = player;
        this.failed = 0;
        this.sum = 0;
    }

    public LootContext unknownContext(Vector3d origin) {
        LootContext.Builder builder = new LootContext.Builder(level).withParameter(LootContextParams.ORIGIN, origin);
        return builder.create(new LootContextParamset.Builder().required(LootContextParams.ORIGIN).build());
    }

    public LootContext chestContext(Vector3d origin, boolean usePlayer) {
        LootContext.Builder builder = new LootContext.Builder(level).withParameter(LootContextParams.ORIGIN, origin);
        if (usePlayer) {
            builder.withParameter(LootContextParams.THIS_ENTITY, player);

        }
        return builder.create(LootContextParamsets.CHEST);
    }

    public <E extends LivingEntity> LootContext entityContext(EntityType<E> entityType, Vector3d origin) {
        E entity = simpleEntity(entityType, new BlockPos(origin));
        LootContext ctx = entity
                .createLootContext(true, DamageSource.playerAttack(player))
                .create(LootContextParamsets.ENTITY);
        yeet(entity);
        return ctx;
    }

    public LootContext blockContext(BlockPos blockPos, boolean usePlayer) {
        LootContext.Builder builder = new LootContext.Builder(level)
                .withRandom(level.getRandom())
                .withParameter(LootContextParams.ORIGIN, Vector3d.atCenterOf(blockPos))
                .withParameter(LootContextParams.TOOL, player.getMainHandItem())
                .withParameter(LootContextParams.BLOCK_STATE, level.getBlockState(blockPos));
        if (usePlayer) {
            builder.withOptionalParameter(LootContextParams.THIS_ENTITY, player);
        }
        return builder.create(LootContextParamsets.BLOCK);
    }

    public LootContext fishingContext(Vector3d origin, int luck) {
        FishingBobberEntity bobber = new FishingBobberEntity(this.level, player, origin.x, origin.y, origin.z);
        LootContext.Builder builder = new LootContext.Builder(level)
                .withParameter(LootContextParams.ORIGIN, origin)
                .withParameter(LootContextParams.TOOL, player.getMainHandItem())
                .withParameter(LootContextParams.THIS_ENTITY, bobber)
                .withOptionalParameter(LootContextParams.KILLER_ENTITY, player)
                .withRandom(level.getRandom())
                .withLuck(luck);
        yeet(bobber);
        return builder.create(LootContextParamsets.FISHING);
    }

    public ILootContextData fillExampleLoot(LootContext context) {
        ILootContextData data = context.getParamOrNull(Constants.DATA);
        assert data != null;
        data.setGeneratedLoot(Stream
                .of(new ItemStack(Items.DIAMOND),
                        new ItemStack(Items.NETHER_BRICK, 10),
                        new ItemStack(Items.GOLDEN_CHESTPLATE))
                .collect(Collectors.toList()));
        return data;
    }

    public <E extends Entity> E simpleEntity(EntityType<E> entityType, BlockPos pos) {
        @SuppressWarnings("unchecked")
        E entity = (E) entityType.spawn(level, null, null, pos, SpawnReason.COMMAND, false, false);
        if (entity == null) {
            throw new RuntimeException("Entity type + '" + entityType.getRegistryName() +
                                       "' cannot be created through 'TestHelper::simpleEntity'");
        }
        return entity;
    }

    public ResourceKey<Biome> biome(ResourceLocation biome) {
        return ResourceKey.create(Registry.BIOME_REGISTRY, Objects.requireNonNull(biome));
    }

    public void yeet(Entity entity) {
        entity.setPos(entity.getX(), -100, entity.getZ());
    }

    public TestHelper shouldSucceed(boolean condition, String msg) {
        debugStack.pushLayer();
        evaluate(condition);
        debugStack.write(condition, "Should succeed with: " + msg);
        debugStack.popLayer();
        return this;
    }

    public TestHelper shouldFail(boolean condition, String msg) {
        debugStack.pushLayer();
        evaluate(!condition);
        debugStack.write(!condition, "Should    fail with: " + msg);
        debugStack.popLayer();
        return this;
    }

    public <E extends Exception> TestHelper shouldThrow(Runnable test, Class<E> exceptionClass, String msg) {
        boolean succeed = false;
        try {
            test.run();
        } catch (Exception exception) {
            if (exceptionClass == exception.getClass()) {
                succeed = true;
            }
        }

        debugStack.pushLayer();
        evaluate(succeed);
        debugStack.write(succeed, "Should   throw with: [" + exceptionClass.getSimpleName() + "] " + msg);
        debugStack.popLayer();
        return this;
    }

    public <T> TestHelper shouldSucceed(Object o, String f, Predicate<T> predicate) {
        @SuppressWarnings("unchecked")
        T value = (T) value(o, f);
        shouldSucceed(predicate.test(value), "Predicate for '" + f + "' matches");
        return this;
    }

    public <T> TestHelper shouldFail(Object o, String f, Predicate<T> predicate) {
        @SuppressWarnings("unchecked")
        T value = (T) value(o, f);
        shouldFail(predicate.test(value), "Predicate for '" + f + "' failed");
        return this;
    }

    private void evaluate(boolean succeed) {
        sum++;
        if (!succeed) failed++;
    }

    public void shouldBeNull(Object o, String f) {
        shouldSucceed(nullValue(o, f), "Value should be NULL for " + f);
    }

    public void shouldBeTrue(Object o, String f) {
        shouldSucceed(trueValue(o, f), "Value should be TRUE for " + f);
    }

    public void shouldBeFalse(Object o, String f) {
        shouldSucceed(falseValue(o, f), "Value should be FALSE for " + f);
    }

    @Nullable
    public Object value(Object o, String f) {
        List<String> collect = Arrays.stream(f.split("/", 1)).collect(Collectors.toList());
        String f_ = collect.remove(0);

        Class<?> c = o.getClass();
        Field field = null;
        do {
            try {
                field = c.getDeclaredField(f_);
                break;
            } catch (Exception ignore) {
            }
        } while ((c = c.getSuperclass()) != null);

        if (field == null) {
            return null;
        }

        try {
            field.setAccessible(true);
            return collect.isEmpty() ? field.get(o) : value(field.get(o), collect.get(0));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean nullValue(Object o, String f) {
        return value(o, f) == null;
    }

    public boolean trueValue(Object o, String f) {
        Object value = value(o, f);
        if (value == null) {
            return false;
        }

        return Boolean.TRUE.equals(value);
    }

    public boolean falseValue(Object o, String f) {
        Object value = value(o, f);
        if (value == null) {
            return false;
        }

        return Boolean.FALSE.equals(value);
    }

    public int getSum() {
        return sum;
    }

    public int getFailed() {
        return failed;
    }
}
