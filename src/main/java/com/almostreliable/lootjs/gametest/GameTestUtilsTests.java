package com.almostreliable.lootjs.gametest;

import com.almostreliable.lootjs.BuildConfig;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(value = BuildConfig.MOD_ID)
@PrefixGameTestTemplate(false)
public class GameTestUtilsTests {

    @GameTest(template = GameTestTemplates.EMPTY)
    public void fillExampleLoot(GameTestHelper helper) {
        helper.succeedIf(() -> {
            Player p = helper.makeMockPlayer();
            LootContext ctx = GameTestUtils.chestContext(helper.getLevel(), p.position(), p);
            var loot = GameTestUtils.fillExampleLoot(ctx);

            ItemStack diamond = loot.get(0);
            ItemStack netherBrick = loot.get(1);
            ItemStack goldenChestPlate = loot.get(2);

            GameTestUtils.assertEquals(helper, diamond.getItem(), Items.DIAMOND);
            GameTestUtils.assertEquals(helper, netherBrick.getItem(), Items.NETHER_BRICK);
            GameTestUtils.assertEquals(helper, netherBrick.getCount(), 10);
            GameTestUtils.assertEquals(helper, goldenChestPlate.getItem(), Items.GOLDEN_CHESTPLATE);
            GameTestUtils.assertTrue(helper, loot.size() == 3, "Should contain 3 items");
        });
    }
}
