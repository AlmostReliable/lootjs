package com.almostreliable.lootjs.gametest.conditions;

import com.almostreliable.lootjs.BuildConfig;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.gametest.GameTestTemplates;
import com.almostreliable.lootjs.gametest.GameTestUtils;
import com.almostreliable.lootjs.loot.modifier.handler.ContainsLootCheck;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(value = BuildConfig.MOD_ID)
@PrefixGameTestTemplate(false)
public class ContainsLootConditionTest {

    private static final Vec3 TEST_POS = new Vec3(0, 0, 0);

    @GameTest(template = GameTestTemplates.EMPTY)
    public void nonExactCheck(GameTestHelper helper) {
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), TEST_POS);
        var loot =GameTestUtils.fillExampleLoot(ctx, Items.DIAMOND, Items.DIAMOND_HELMET);
        ContainsLootCheck c = new ContainsLootCheck(itemStack -> itemStack.getItem() ==
                                                                 Items.DIAMOND, false);
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper, c.apply(ctx, loot), "Should pass"));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void exactCheck(GameTestHelper helper) {
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), TEST_POS);
        var loot = GameTestUtils.fillExampleLoot(ctx, Items.DIAMOND, Items.DIAMOND_HELMET);
        ContainsLootCheck c = new ContainsLootCheck(itemStack -> itemStack.getItem() == Items.DIAMOND ||
                                                                         itemStack.getItem() ==
                                                                         Items.DIAMOND_HELMET, true);
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper, c.apply(ctx, loot), "Should pass"));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void exactCheck_fail(GameTestHelper helper) {
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), TEST_POS);
        var loot = GameTestUtils.fillExampleLoot(ctx, Items.DIAMOND, Items.DIAMOND_HELMET);
        ContainsLootCheck c = new ContainsLootCheck(ItemFilter.ARMOR, true);
        helper.succeedIf(() -> GameTestUtils.assertFalse(helper,
                c.apply(ctx, loot),
                "Should fail, because of diamond is not armor"));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void simpleCheck_fail(GameTestHelper helper) {
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), TEST_POS);
        var loot = GameTestUtils.fillExampleLoot(ctx, Items.DIAMOND, Items.DIAMOND_HELMET);
        ContainsLootCheck c = new ContainsLootCheck(itemStack -> itemStack.getItem() ==
                                                                 Items.NETHER_BRICK, false);
        helper.succeedIf(() -> GameTestUtils.assertFalse(helper, c.apply(ctx, loot), "Should fail"));
    }
}
