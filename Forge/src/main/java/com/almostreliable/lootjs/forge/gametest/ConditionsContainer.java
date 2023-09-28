package com.almostreliable.lootjs.forge.gametest;

import com.almostreliable.lootjs.BuildConfig;
import com.almostreliable.lootjs.core.ILootCondition;
import com.almostreliable.lootjs.kube.LootConditionsContainer;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(value = BuildConfig.MOD_ID)
@PrefixGameTestTemplate(false)
public class ConditionsContainer {

    @GameTest(template = GameTestTemplates.EMPTY)
    public void entityTarget_Entity(GameTestHelper helper) {
        helper.succeedIf(() -> {
            TestLootConditionsContainer conditions = new TestLootConditionsContainer();
            conditions.matchEntity(e -> {});
            GameTestUtils.assertEquals(helper,
                    conditions.<LootItemEntityPropertyCondition>last().entityTarget,
                    LootContext.EntityTarget.THIS);
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void entityTarget_Killer(GameTestHelper helper) {
        helper.succeedIf(() -> {
            TestLootConditionsContainer conditions = new TestLootConditionsContainer();
            conditions.matchKiller(e -> {});
            GameTestUtils.assertEquals(helper,
                    conditions.<LootItemEntityPropertyCondition>last().entityTarget,
                    LootContext.EntityTarget.KILLER);
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void entityTarget_DirectKiller(GameTestHelper helper) {
        helper.succeedIf(() -> {
            TestLootConditionsContainer conditions = new TestLootConditionsContainer();
            conditions.matchDirectKiller(e -> {});
            GameTestUtils.assertEquals(helper,
                    conditions.<LootItemEntityPropertyCondition>last().entityTarget,
                    LootContext.EntityTarget.DIRECT_KILLER);
        });
    }

    public static class TestLootConditionsContainer implements LootConditionsContainer<TestLootConditionsContainer> {
        private ILootCondition last;

        @Override
        public TestLootConditionsContainer addCondition(LootItemCondition condition) {
            this.last = (ILootCondition) condition;
            return this;
        }

        public <T extends LootItemCondition> T last() {
            //noinspection unchecked
            return (T) last;
        }
    }
}
