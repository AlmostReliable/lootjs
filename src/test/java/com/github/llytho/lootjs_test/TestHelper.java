package com.github.llytho.lootjs_test;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.DebugStack;
import com.github.llytho.lootjs.core.ILootContextData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestHelper {
    public final ServerLevel level;
    public final ServerPlayer player;
    public final DebugStack debugStack;
    private int sum;
    private int failed;

    public TestHelper(DebugStack debugStack, ServerLevel level, ServerPlayer player) {
        this.debugStack = debugStack;
        this.level = level;
        this.player = player;
        this.failed = 0;
        this.sum = 0;
    }

    public LootContext unknownContext(Vec3 origin) {
        LootContext.Builder builder = new LootContext.Builder(level).withParameter(LootContextParams.ORIGIN, origin);
        return builder.create(new LootContextParamSet.Builder().required(LootContextParams.ORIGIN).build());
    }

    public LootContext chestContext(Vec3 origin, boolean usePlayer) {
        LootContext.Builder builder = new LootContext.Builder(level).withParameter(LootContextParams.ORIGIN, origin);
        if (usePlayer) {
            builder.withParameter(LootContextParams.THIS_ENTITY, player);

        }
        return builder.create(LootContextParamSets.CHEST);
    }

    public <E extends LivingEntity> LootContext entityContext(EntityType<E> entityType, Vec3 origin) {
        E entity = simpleEntity(entityType, new BlockPos(origin));
        LootContext ctx = entity
                .createLootContext(true, DamageSource.playerAttack(player))
                .create(LootContextParamSets.ENTITY);
        yeet(entity);
        return ctx;
    }

    public LootContext blockContext(BlockPos blockPos, boolean usePlayer) {
        LootContext.Builder builder = new LootContext.Builder(level)
                .withRandom(level.getRandom())
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockPos))
                .withParameter(LootContextParams.TOOL, player.getMainHandItem())
                .withParameter(LootContextParams.BLOCK_STATE, level.getBlockState(blockPos));
        if (usePlayer) {
            builder.withOptionalParameter(LootContextParams.THIS_ENTITY, player);
        }
        return builder.create(LootContextParamSets.BLOCK);
    }

    public LootContext fishingContext(Vec3 origin, int luck) {
        FishingHook bobber = new FishingHook(player, this.level, luck, 0);
        LootContext.Builder builder = new LootContext.Builder(level)
                .withParameter(LootContextParams.ORIGIN, origin)
                .withParameter(LootContextParams.TOOL, player.getMainHandItem())
                .withParameter(LootContextParams.THIS_ENTITY, bobber)
                .withOptionalParameter(LootContextParams.KILLER_ENTITY, player)
                .withRandom(level.getRandom())
                .withLuck(luck);
        yeet(bobber);
        return builder.create(LootContextParamSets.FISHING);
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
        E entity = (E) entityType.spawn(level, null, null, pos, MobSpawnType.COMMAND, false, false);
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
