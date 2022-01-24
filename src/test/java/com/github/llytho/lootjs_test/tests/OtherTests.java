package com.github.llytho.lootjs_test.tests;

import com.github.llytho.lootjs.util.TagOrEntry;
import com.github.llytho.lootjs.util.Utils;
import com.github.llytho.lootjs_test.AllTests;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

public class OtherTests {
    public static void loadTests() {
        AllTests.add("Utils::getTagOrEntry", helper -> {
            helper.debugStack.pushLayer();

            helper.debugStack.h2("minecraft:stone as id");
            TagOrEntry<Block> stoneEntry = Utils.getTagOrEntry(ForgeRegistries.BLOCKS, "minecraft:stone");
            helper.shouldSucceed(!stoneEntry.isTag(), "Stone is not a tag");
            helper.shouldSucceed(stoneEntry.entry == Blocks.STONE, "Entry is Blocks.STONE");
            helper.shouldSucceed(stoneEntry.tag == null, "Tag is null");

            helper.debugStack.h2("#forge:ores as tag");
            TagOrEntry<Item> oresTag = Utils.getTagOrEntry(ForgeRegistries.ITEMS, "#forge:ores");
            helper.shouldSucceed(oresTag.isTag(), "Ores is a tag");
            helper.shouldSucceed(oresTag.entry == null, "Entry is null");
            helper.shouldSucceed(oresTag.tag.contains(Items.IRON_ORE), "Tag is ORES");

            helper.debugStack.h2("Invalid tag or entry");
            helper.shouldThrow(() -> Utils.getTagOrEntry(ForgeRegistries.ITEMS, "#not_a_tag"),
                    IllegalArgumentException.class,
                    "Tag '#not_a_tag' does not exist");
            helper.shouldThrow(() -> Utils.getTagOrEntry(ForgeRegistries.ITEMS, "relentless_cant_read_this"),
                    IllegalArgumentException.class,
                    "Entry 'relentless_cant_read_this' does not exist");
            helper.debugStack.popLayer();
        });
    }
}
