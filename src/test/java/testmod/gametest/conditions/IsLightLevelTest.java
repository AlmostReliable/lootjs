package testmod.gametest.conditions;

import com.almostreliable.lootjs.BuildConfig;
import testmod.gametest.GameTestTemplates;
import testmod.gametest.GameTestUtils;
import com.almostreliable.lootjs.loot.condition.IsLightLevel;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(value = BuildConfig.MOD_ID)
@PrefixGameTestTemplate(false)
public class IsLightLevelTest {
    private static final Vec3 TEST_POS = new Vec3(0, 1, 0);

    @GameTest(template = GameTestTemplates.EMPTY)
    public void matchLight(GameTestHelper helper) {
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), TEST_POS);

        IsLightLevel isLightLevel = new IsLightLevel(0, 15);
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper,
                isLightLevel.test(ctx),
                "IsLightLevel check should pass"));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void failMatchLight(GameTestHelper helper) {
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), TEST_POS);

        IsLightLevel isLightLevel = new IsLightLevel(15, 15);
        helper.succeedIf(() -> GameTestUtils.assertFalse(helper,
                isLightLevel.test(ctx),
                "IsLightLevel check should fail"));
    }
}
