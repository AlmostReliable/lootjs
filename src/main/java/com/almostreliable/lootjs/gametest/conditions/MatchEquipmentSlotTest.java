package com.almostreliable.lootjs.gametest.conditions;

import com.almostreliable.lootjs.BuildConfig;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import com.almostreliable.lootjs.gametest.GameTestTemplates;
import com.almostreliable.lootjs.gametest.GameTestUtils;
import com.almostreliable.lootjs.loot.condition.MatchEquipmentSlot;
import com.mojang.authlib.GameProfile;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.util.UUID;

@GameTestHolder(value = BuildConfig.MOD_ID)
@PrefixGameTestTemplate(false)
public class MatchEquipmentSlotTest {

    @GameTest(template = GameTestTemplates.EMPTY)
    public void hasDiamondInHand(GameTestHelper helper) {
        LootContext ctx = basicSetup(helper);
        MatchEquipmentSlot check = new MatchEquipmentSlot(EquipmentSlot.MAINHAND,
                itemStack -> itemStack.getItem() == Items.DIAMOND);
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper,
                check.test(ctx),
                "MatchEquipmentSlot check should pass"));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void hasStickInOffHand(GameTestHelper helper) {
        LootContext ctx = basicSetup(helper);
        MatchEquipmentSlot check = new MatchEquipmentSlot(EquipmentSlot.OFFHAND,
                itemStack -> itemStack.getItem() == Items.STICK);
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper,
                check.test(ctx),
                "MatchEquipmentSlot check should pass"));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void matchBoots(GameTestHelper helper) {
        LootContext ctx = basicSetup(helper);
        MatchEquipmentSlot check = new MatchEquipmentSlot(EquipmentSlot.FEET,
                itemStack -> itemStack.getItem() == Items.DIAMOND_BOOTS);
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper,
                check.test(ctx),
                "MatchEquipmentSlot check should pass"));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void failMatchMainHand(GameTestHelper helper) {
        LootContext ctx = basicSetup(helper);
        MatchEquipmentSlot check = new MatchEquipmentSlot(EquipmentSlot.MAINHAND, ItemFilter.SWORD);
        helper.succeedIf(() -> GameTestUtils.assertFalse(helper,
                check.test(ctx),
                "MatchEquipmentSlot check should fail"));
    }

    public LootContext basicSetup(GameTestHelper helper) {
        ServerPlayer player = new ServerPlayer(helper.getLevel().getServer(),
                helper.getLevel(),
                new GameProfile(UUID.randomUUID(), "mr_testi"),
                ClientInformation.createDefault()) {

            @Override
            public boolean isSpectator() {
                return false;
            }

            @Override
            public boolean isCreative() {
                return true;
            }
        };

        var params = new LootParams.Builder(helper.getLevel())
                .withParameter(LootContextParams.ORIGIN, player.position())
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .create(LootContextParamSets.CHEST);
        LootContext lc = new LootContext.Builder(params).create(null);

        player.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND));
        player.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.STICK));
        player.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
        player.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
        player.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
        player.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));

        return lc;
    }
}
