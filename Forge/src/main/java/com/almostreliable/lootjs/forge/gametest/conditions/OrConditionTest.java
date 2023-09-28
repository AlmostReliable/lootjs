package com.almostreliable.lootjs.forge.gametest.conditions;

import com.almostreliable.lootjs.BuildConfig;
import com.almostreliable.lootjs.forge.gametest.GameTestTemplates;
import com.almostreliable.lootjs.forge.gametest.GameTestUtils;
import com.almostreliable.lootjs.loot.condition.AndCondition;
import com.almostreliable.lootjs.loot.condition.AnyDimension;
import com.almostreliable.lootjs.loot.condition.IsLightLevel;
import com.almostreliable.lootjs.loot.condition.OrCondition;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(value = BuildConfig.MOD_ID)
@PrefixGameTestTemplate(false)
public class OrConditionTest {
    private static final Vec3 TEST_POS = new Vec3(0, 0, 0);

    @GameTest(template = GameTestTemplates.EMPTY)
    public void orConditionSucceed(GameTestHelper helper) {
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), TEST_POS);
        OrCondition or = new OrCondition(new AnyDimension(new ResourceLocation[]{ new ResourceLocation("overworld") }),
                new IsLightLevel(0, 15));
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper, or.test(ctx), "OrCondition check should pass"));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void orConditionFails(GameTestHelper helper) {
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), TEST_POS);
        OrCondition or = new OrCondition(new AnyDimension(new ResourceLocation[]{ new ResourceLocation("nether") }),
                new AnyDimension(new ResourceLocation[]{ new ResourceLocation("end") }));
        helper.succeedIf(() -> GameTestUtils.assertFalse(helper, or.test(ctx), "OrCondition check should fail"));
    }
}
