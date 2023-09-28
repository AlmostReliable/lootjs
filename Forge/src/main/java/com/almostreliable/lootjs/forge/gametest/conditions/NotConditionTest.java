package com.almostreliable.lootjs.forge.gametest.conditions;

import com.almostreliable.lootjs.BuildConfig;
import com.almostreliable.lootjs.forge.gametest.GameTestTemplates;
import com.almostreliable.lootjs.forge.gametest.GameTestUtils;
import com.almostreliable.lootjs.loot.condition.AnyDimension;
import com.almostreliable.lootjs.loot.condition.IsLightLevel;
import com.almostreliable.lootjs.loot.condition.NotCondition;
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
public class NotConditionTest {
    private static final Vec3 TEST_POS = new Vec3(0, 0, 0);

    @GameTest(template = GameTestTemplates.EMPTY)
    public void notConditionSucceed(GameTestHelper helper) {
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), TEST_POS);
        NotCondition not = new NotCondition(new AnyDimension(new ResourceLocation[]{
                new ResourceLocation("nether")
        }));
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper, not.test(ctx), "NotCondition check should pass"));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void notConditionFails(GameTestHelper helper) {
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), TEST_POS);
        NotCondition not = new NotCondition(new AnyDimension(new ResourceLocation[]{
                new ResourceLocation("overworld")
        }));
        helper.succeedIf(() -> GameTestUtils.assertFalse(helper, not.test(ctx), "NotCondition check should fail"));
    }
}
