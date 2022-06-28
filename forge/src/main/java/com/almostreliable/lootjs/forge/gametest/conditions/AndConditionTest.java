package com.almostreliable.lootjs.forge.gametest.conditions;

import com.almostreliable.lootjs.BuildConfig;
import com.almostreliable.lootjs.forge.gametest.GameTestTemplates;
import com.almostreliable.lootjs.forge.gametest.GameTestUtils;
import com.almostreliable.lootjs.loot.condition.AndCondition;
import com.almostreliable.lootjs.loot.condition.AnyDimension;
import com.almostreliable.lootjs.loot.condition.IsLightLevel;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(value = BuildConfig.MOD_ID)
@PrefixGameTestTemplate(false)
public class AndConditionTest {
    private static final Vec3 TEST_POS = new Vec3(0, 0, 0);

    @GameTest(template = GameTestTemplates.EMPTY)
    public void andConditionSucceed(GameTestHelper helper) {
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), TEST_POS);
        AndCondition ac = new AndCondition(new AnyDimension(new ResourceLocation[]{ new ResourceLocation("overworld") }),
                new IsLightLevel(0, 15));
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper, ac.test(ctx), "AndCondition check should pass"));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void andConditionFails(GameTestHelper helper) {
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), TEST_POS);
        AndCondition ac = new AndCondition(new AnyDimension(new ResourceLocation[]{ new ResourceLocation("overworld") }),
                new IsLightLevel(0, 7),
                new IsLightLevel(8, 15));
        helper.succeedIf(() -> GameTestUtils.assertFalse(helper, ac.test(ctx), "AndCondition check should fail"));
    }
}
