package com.github.llytho.lootjs_test.tests;

import com.github.llytho.lootjs.core.ILootCondition;
import com.github.llytho.lootjs.filters.Resolver;
import com.github.llytho.lootjs.kube.LootConditionsContainer;
import com.github.llytho.lootjs.loot.condition.AnyBiomeCheck;
import com.github.llytho.lootjs.loot.condition.AnyStructure;
import com.github.llytho.lootjs.loot.condition.BiomeCheck;
import com.github.llytho.lootjs_test.AllTests;
import com.github.llytho.lootjs_test.TestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

public class ConditionsContainerTests {
    private static final TestLootConditionsContainer conditions = new TestLootConditionsContainer();

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
        helper.shouldSucceed(
                conditions.<LootItemEntityPropertyCondition>last().entityTarget == LootContext.EntityTarget.THIS,
                "EntityTarget is = " + LootContext.EntityTarget.THIS.name());

        conditions.matchKiller(e -> {});
        helper.shouldSucceed(
                conditions.<LootItemEntityPropertyCondition>last().entityTarget == LootContext.EntityTarget.KILLER,
                "EntityTarget is = " + LootContext.EntityTarget.KILLER.name());

        conditions.matchDirectKiller(e -> {});
        helper.shouldSucceed(conditions.<LootItemEntityPropertyCondition>last().entityTarget ==
                             LootContext.EntityTarget.DIRECT_KILLER,
                "EntityTarget is = " + LootContext.EntityTarget.DIRECT_KILLER.name());
    }

    private static void anyStructure(TestHelper helper) {
        helper.debugStack.h2("anyStructure");
        conditions.anyStructure(new ResourceLocation[]{
                new ResourceLocation("stronghold"), new ResourceLocation("village_plains")
        }, true);
        helper.shouldSucceed(conditions.last instanceof AnyStructure, "AnyStructure instance");
        helper.shouldSucceed(conditions.<AnyStructure>last().getStructuresOld().size() == 2,
                "Should have 2 structures");
        helper.shouldThrow(() -> conditions.anyStructure(new ResourceLocation[]{
                new ResourceLocation("wrong_structure")
        }, true), IllegalArgumentException.class, "'wrong_structure' does not exist");
    }

    private static void anyBiome(TestHelper helper) {
        helper.debugStack.h2("anyBiome");

        conditions.anyBiome(Resolver.of("minecraft:desert"),
                Resolver.of("#nether"),
                Resolver.of("minecraft:jungle"));
        helper.shouldSucceed(conditions.last instanceof AnyBiomeCheck, "AnyBiomeCheck instance");
        helper.shouldSucceed(conditions.<AnyBiomeCheck>last().getBiomes().size() == 2, "Should have 2 biomes");
        helper.shouldSucceed(conditions.<AnyBiomeCheck>last().getTags().size() == 1, "Should have 1 type");

        // TODO: Currently, not available anymore. LootJS will just take the input.
//        helper.shouldThrow(() -> conditions.anyBiome(TagKeyOrEntryResolver.of("not_existing_biome")),
//                IllegalStateException.class,
//                "'not_existing_biome' does not exist");
//        helper.shouldThrow(() -> conditions.anyBiome(TagKeyOrEntryResolver.of("#wrong_type")),
//                IllegalStateException.class,
//                "'#wrong_type' does not exist");
    }

    private static void biome(TestHelper helper) {
        helper.debugStack.h2("biome");
        conditions.biome(Resolver.of("minecraft:desert"),
                Resolver.of("#nether"),
                Resolver.of("minecraft:jungle"));
        helper.shouldSucceed(conditions.last instanceof BiomeCheck, "BiomeCheck instance");
        helper.shouldSucceed(conditions.<BiomeCheck>last().getBiomes().size() == 2, "Should have 2 biomes");
        helper.shouldSucceed(conditions.<BiomeCheck>last().getTags().size() == 1, "Should have 1 type");

        // TODO: Currently, not available anymore. LootJS will just take the input.
//        helper.shouldThrow(() -> conditions.biome(TagKeyOrEntryResolver.of("not_existing_biome")),
//                IllegalStateException.class,
//                "'not_existing_biome' does not exist");
//        helper.shouldThrow(() -> conditions.biome(TagKeyOrEntryResolver.of("#wrong_type")),
//                IllegalStateException.class,
//                "'#wrong_type' does not exist");
    }

    public static class TestLootConditionsContainer implements LootConditionsContainer<TestLootConditionsContainer> {

        private ILootCondition last;

        @Override
        public TestLootConditionsContainer addCondition(ILootCondition condition) {
            this.last = condition;
            return this;
        }

        public <T extends LootItemCondition> T last() {
            //noinspection unchecked
            return (T) last;
        }
    }
}
