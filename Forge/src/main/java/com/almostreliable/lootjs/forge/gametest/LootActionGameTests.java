package com.almostreliable.lootjs.forge.gametest;

import com.almostreliable.lootjs.BuildConfig;
import com.almostreliable.lootjs.core.ILootContextData;
import com.almostreliable.lootjs.loot.action.AddLootAction;
import com.almostreliable.lootjs.loot.action.RemoveLootAction;
import com.almostreliable.lootjs.loot.action.ReplaceLootAction;
import com.almostreliable.lootjs.loot.table.entry.LootEntry;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

// TODO lightning strike & explosion test currently not working. No clue how to handle async stuff in GameTest.
@GameTestHolder(value = BuildConfig.MOD_ID)
@PrefixGameTestTemplate(false)
public class LootActionGameTests {


    @GameTest(template = GameTestTemplates.EMPTY)
    public void addLootAction(GameTestHelper helper) {
        helper.succeedIf(() -> {
            Player p = helper.makeMockPlayer();
            LootContext ctx = GameTestUtils.chestContext(helper.getLevel(), p.position(), p);
            ILootContextData data = GameTestUtils.fillExampleLoot(ctx);
            GameTestUtils.assertFalse(helper,
                    data.getGeneratedLoot().stream().anyMatch(i -> i.getItem().equals(Items.APPLE)),
                    "Should contains no apples");
            GameTestUtils.assertFalse(helper,
                    data.getGeneratedLoot().stream().anyMatch(i -> i.getItem().equals(Items.LANTERN)),
                    "Should contains no lanterns");
            AddLootAction action = new AddLootAction(new LootEntry[]{
                    LootEntry.ofItemStack(Items.APPLE.getDefaultInstance()),
                    LootEntry.ofItemStack(Items.LANTERN.getDefaultInstance())
            });
            action.applyLootHandler(ctx, data.getGeneratedLoot());
            GameTestUtils.assertTrue(helper,
                    data.getGeneratedLoot().stream().anyMatch(i -> i.getItem().equals(Items.APPLE)),
                    "Should contains apples now");
            GameTestUtils.assertTrue(helper,
                    data.getGeneratedLoot().stream().anyMatch(i -> i.getItem().equals(Items.LANTERN)),
                    "Should contains lanterns now");
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void removeLootAction(GameTestHelper helper) {
        helper.succeedIf(() -> {
            Player p = helper.makeMockPlayer();
            LootContext ctx = GameTestUtils.chestContext(helper.getLevel(), p.position(), p);
            ILootContextData data = GameTestUtils.fillExampleLoot(ctx);

            RemoveLootAction action = new RemoveLootAction(i -> i.getItem().equals(Items.DIAMOND));
            action.applyLootHandler(ctx, data.getGeneratedLoot());
            GameTestUtils.assertFalse(helper,
                    data.getGeneratedLoot().stream().anyMatch(i -> i.getItem().equals(Items.DIAMOND)),
                    "Diamond does not exist anymore");
            GameTestUtils.assertTrue(helper,
                    data.getGeneratedLoot().size() == 2,
                    "2 items in loot pool after removing diamond");
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void replaceLootAction(GameTestHelper helper) {
        helper.succeedIf(() -> {
            Player p = helper.makeMockPlayer();
            LootContext ctx = GameTestUtils.chestContext(helper.getLevel(), p.position(), p);
            ILootContextData data = GameTestUtils.fillExampleLoot(ctx);

            GameTestUtils.assertTrue(helper,
                    data.getGeneratedLoot().stream().anyMatch(i -> i.getItem().equals(Items.DIAMOND)),
                    "Diamond exist");
            GameTestUtils.assertFalse(helper,
                    data.getGeneratedLoot().stream().anyMatch(i -> i.getItem().equals(Items.MAGMA_CREAM)),
                    "Magma cream does not exist currently");

            ReplaceLootAction action = new ReplaceLootAction(i -> i.getItem().equals(Items.DIAMOND),
                    LootEntry.ofItemStack(Items.MAGMA_CREAM.getDefaultInstance()), false);
            action.applyLootHandler(ctx, data.getGeneratedLoot());

            GameTestUtils.assertFalse(helper,
                    data.getGeneratedLoot().stream().anyMatch(i -> i.getItem().equals(Items.DIAMOND)),
                    "Diamond does not exist anymore");
            GameTestUtils.assertTrue(helper,
                    data.getGeneratedLoot().stream().anyMatch(i -> i.getItem().equals(Items.MAGMA_CREAM)),
                    "Magma cream exists now");
            GameTestUtils.assertTrue(helper, data.getGeneratedLoot().size() == 3, "still 3 items in loot pool");
        });
    }
}
