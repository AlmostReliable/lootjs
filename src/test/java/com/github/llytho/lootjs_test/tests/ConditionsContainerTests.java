package com.github.llytho.lootjs_test.tests;

import com.github.llytho.lootjs.kube.ConditionsContainer;
import com.github.llytho.lootjs.loot.condition.AnyBiomeCheck;
import com.github.llytho.lootjs.loot.condition.AnyStructure;
import com.github.llytho.lootjs.loot.condition.BiomeCheck;
import com.github.llytho.lootjs_test.AllTests;
import com.github.llytho.lootjs_test.TestHelper;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.common.BiomeDictionary;

import java.util.HashMap;
import java.util.List;

public class ConditionsContainerTests {
    private static final TestConditionsContainer conditions = new TestConditionsContainer();

    public static void loadTests() {


        AllTests.add("ConditionsContainer", helper -> {
            helper.debugStack.pushLayer();

            biome(helper);
            anyBiome(helper);
            anyStructure(helper);
            entityTargets(helper);
        });

    }

    private static void entityTargets(TestHelper helper) {
        helper.debugStack.h2("entity targets");
        conditions.matchEntity(e -> {});
        helper.<LootContext.EntityTarget>shouldSucceed(conditions.last, "entityTarget", entityTarget -> {
            return entityTarget == LootContext.EntityTarget.THIS;
        });

        conditions.matchKiller(e -> {});
        helper.<LootContext.EntityTarget>shouldSucceed(conditions.last, "entityTarget", entityTarget -> {
            return entityTarget == LootContext.EntityTarget.KILLER;
        });

        conditions.matchDirectKiller(e -> {});
        helper.<LootContext.EntityTarget>shouldSucceed(conditions.last, "entityTarget", entityTarget -> {
            return entityTarget == LootContext.EntityTarget.DIRECT_KILLER;
        });
    }

    private static void anyStructure(TestHelper helper) {
        helper.debugStack.h2("anyStructure");
        conditions.anyStructure(new ResourceLocation[]{
                new ResourceLocation("stronghold"), new ResourceLocation("village")
        }, true);
        helper.shouldSucceed(conditions.last instanceof AnyStructure, "AnyStructure instance");
        helper.<Structure<?>[]>shouldSucceed(conditions.last, "structures", structures -> {
            return structures.length == 2;
        });
        helper.shouldThrow(() -> {
            conditions.anyStructure(new ResourceLocation[]{
                    new ResourceLocation("wrong_structure")
            }, true);
        }, IllegalStateException.class, "'wrong_structure' does not exist");
    }

    private static void anyBiome(TestHelper helper) {
        helper.debugStack.h2("anyBiome");
        conditions.anyBiome("minecraft:desert", "#nether", "minecraft:jungle");
        helper.shouldSucceed(conditions.last instanceof AnyBiomeCheck, "AnyBiomeCheck instance");
        helper.<List<RegistryKey<Biome>>>shouldSucceed(conditions.last, "biomes", biomes -> {
            return biomes.size() == 2;
        });
        helper.<List<BiomeDictionary.Type>>shouldSucceed(conditions.last, "types", types -> {
            return types.size() == 1;
        });
        helper.shouldThrow(() -> {
            conditions.anyBiome("not_existing_biome");
        }, IllegalStateException.class, "'not_existing_biome' does not exist");
        helper.shouldThrow(() -> {
            conditions.anyBiome("#wrong_type");
        }, IllegalStateException.class, "'#wrong_type' does not exist");
    }

    private static void biome(TestHelper helper) {
        helper.debugStack.h2("biome");
        conditions.biome("minecraft:desert", "#nether", "minecraft:jungle");
        helper.shouldSucceed(conditions.last instanceof BiomeCheck, "BiomeCheck instance");
        helper.<List<RegistryKey<Biome>>>shouldSucceed(conditions.last, "biomes", biomes -> {
            return biomes.size() == 2;
        });
        helper.<List<BiomeDictionary.Type>>shouldSucceed(conditions.last, "types", types -> {
            return types.size() == 1;
        });
        helper.shouldThrow(() -> {
            conditions.biome("not_existing_biome");
        }, IllegalStateException.class, "'not_existing_biome' does not exist");
        helper.shouldThrow(() -> {
            conditions.biome("#wrong_type");
        }, IllegalStateException.class, "'#wrong_type' does not exist");
    }

    public static class TestConditionsContainer implements ConditionsContainer<TestConditionsContainer> {

        public ILootCondition last;

        @Override
        public TestConditionsContainer addCondition(ILootCondition pCondition) {
            this.last = pCondition;
            return this;
        }
    }
}
