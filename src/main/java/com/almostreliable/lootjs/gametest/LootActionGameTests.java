package com.almostreliable.lootjs.gametest;

import com.almostreliable.lootjs.BuildConfig;
import com.almostreliable.lootjs.core.LootBucket;
import com.almostreliable.lootjs.core.entry.LootEntry;
import com.almostreliable.lootjs.loot.modifier.handler.AddLootAction;
import com.almostreliable.lootjs.loot.modifier.handler.RemoveLootAction;
import com.almostreliable.lootjs.loot.modifier.handler.ReplaceLootAction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

// TODO lightning strike & explosion test currently not working. No clue how to handle async stuff in GameTest.
@GameTestHolder(value = BuildConfig.MOD_ID)
@PrefixGameTestTemplate(false)
public class LootActionGameTests {


    @GameTest(template = GameTestTemplates.EMPTY)
    public void addLootAction(GameTestHelper helper) {
        helper.succeedIf(() -> {
            Player p = helper.makeMockPlayer();
            LootContext ctx = GameTestUtils.chestContext(helper.getLevel(), p.position(), p);
            LootBucket loot = GameTestUtils.fillExampleLoot(ctx);
            GameTestUtils.assertFalse(helper,
                    loot.hasItem(itemStack -> itemStack.getItem().equals(Items.APPLE)),
                    "Should contains no apples");
            GameTestUtils.assertFalse(helper,
                    loot.hasItem(itemStack -> itemStack.getItem().equals(Items.LANTERN)),
                    "Should contains no lanterns");
            AddLootAction action = new AddLootAction(
                    LootEntry.of(Items.APPLE.getDefaultInstance()),
                    LootEntry.of(Items.LANTERN.getDefaultInstance())
            );
            action.apply(ctx, loot);
            GameTestUtils.assertTrue(helper,
                    loot.hasItem(itemStack -> itemStack.getItem().equals(Items.APPLE)),
                    "Should contains apples now");
            GameTestUtils.assertTrue(helper,
                    loot.hasItem(itemStack -> itemStack.getItem().equals(Items.LANTERN)),
                    "Should contains lanterns now");
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void removeLootAction(GameTestHelper helper) {
        helper.succeedIf(() -> {
            Player p = helper.makeMockPlayer();
            LootContext ctx = GameTestUtils.chestContext(helper.getLevel(), p.position(), p);
            var loot = GameTestUtils.fillExampleLoot(ctx);

            RemoveLootAction action = new RemoveLootAction(i -> i.getItem().equals(Items.DIAMOND));
            action.apply(ctx, loot);
            GameTestUtils.assertFalse(helper,
                    loot.hasItem(i -> i.getItem().equals(Items.DIAMOND)),
                    "Diamond does not exist anymore");
            GameTestUtils.assertTrue(helper,
                    loot.size() == 2,
                    "2 items in loot pool after removing diamond");
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void replaceLootAction(GameTestHelper helper) {
        helper.succeedIf(() -> {
            Player p = helper.makeMockPlayer();
            LootContext ctx = GameTestUtils.chestContext(helper.getLevel(), p.position(), p);
            var loot = GameTestUtils.fillExampleLoot(ctx);

            GameTestUtils.assertTrue(helper,
                    loot.hasItem(i -> i.getItem().equals(Items.DIAMOND)),
                    "Diamond exist");
            GameTestUtils.assertFalse(helper,
                    loot.hasItem(i -> i.getItem().equals(Items.MAGMA_CREAM)),
                    "Magma cream does not exist currently");

            ReplaceLootAction action = new ReplaceLootAction(i -> i.getItem().equals(Items.DIAMOND),
                    LootEntry.of(Items.MAGMA_CREAM.getDefaultInstance()), false);
            action.apply(ctx, loot);

            GameTestUtils.assertFalse(helper,
                    loot.hasItem(i -> i.getItem().equals(Items.DIAMOND)),
                    "Diamond does not exist anymore");
            GameTestUtils.assertTrue(helper,
                    loot.hasItem(i -> i.getItem().equals(Items.MAGMA_CREAM)),
                    "Magma cream exists now");
            GameTestUtils.assertTrue(helper, loot.size() == 3, "still 3 items in loot pool");
        });
    }
}
