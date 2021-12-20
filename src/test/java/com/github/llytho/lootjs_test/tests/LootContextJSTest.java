package com.github.llytho.lootjs_test.tests;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootContextData;
import com.github.llytho.lootjs.kube.LootContextJS;
import com.github.llytho.lootjs_test.AllTests;
import com.github.llytho.lootjs_test.TestHelper;
import dev.latvian.kubejs.item.ItemStackJS;
import dev.latvian.kubejs.item.ingredient.IngredientJS;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LootContextJSTest {
    public static void loadTests() {
        AllTests.add("LootContextJS", helper -> {
            helper.debugStack.pushLayer();

            BlockPos blockPos = helper.player.blockPosition().below();
            BlockState blockState = helper.level.getBlockState(blockPos);

            LootContextJS entityCtx = new LootContextJS(helper.entityContext(EntityType.CREEPER,
                    helper.player.position()));
            LootContextJS blockCtx = new LootContextJS(helper.blockContext(blockPos, true));
            LootContextJS chestCtx = new LootContextJS(helper.chestContext(helper.player.position(), true));
            LootContextJS fishingCtx = new LootContextJS(helper.fishingContext(helper.player.position(), 2));
            LootContextJS unknownCtx = new LootContextJS(helper.unknownContext(helper.player.position()));

            blockDetection(helper, entityCtx, blockCtx, chestCtx, fishingCtx, unknownCtx, blockState);
            entityDetection(helper, entityCtx, blockCtx, chestCtx, fishingCtx, unknownCtx);
            killerEntityDetection(helper, entityCtx, blockCtx, chestCtx, fishingCtx, unknownCtx);
            playerDetection(helper, entityCtx, blockCtx, chestCtx, fishingCtx, unknownCtx);
            lootHandling(helper, entityCtx);
            helper.debugStack.popLayer();
        });
    }

    private static void lootHandling(TestHelper helper, LootContextJS entityCtx) {
        helper.debugStack.h2("Loot tests");

        helper.fillExampleLoot(entityCtx.getVanillaContext());

        helper.shouldSucceed(entityCtx.lootSize() == 3, "Item size -> 3");
        helper.shouldSucceed(entityCtx.hasLoot(IngredientJS.of("minecraft:diamond")), "Diamond exist in loot");

        helper.shouldFail(entityCtx.hasLoot(IngredientJS.of("minecraft:stick")), "No sticks");
        entityCtx.addLoot(ItemStackJS.of("minecraft:stick"));
        helper.shouldSucceed(entityCtx.hasLoot(IngredientJS.of("minecraft:stick")), "Sticks now exists after adding");
        helper.shouldSucceed(entityCtx.lootSize() == 4, "Item size now 4 after adding stick");
        entityCtx.removeLoot(ItemStackJS.of("minecraft:stick"));
        helper.shouldFail(entityCtx.hasLoot(IngredientJS.of("minecraft:stick")), "Sticks removed");
        helper.shouldSucceed(entityCtx.lootSize() == 3, "Item size now 3 again after removing stick");

        ArrayList<ItemStackJS> loopedItems = new ArrayList<>();
        entityCtx.forEachLoot(loopedItems::add);
        helper.shouldSucceed(loopedItems.stream().allMatch(entityCtx::hasLoot), "forEach works");
    }

    private static void killerEntityDetection(TestHelper helper, LootContextJS entityCtx, LootContextJS blockCtx, LootContextJS chestCtx, LootContextJS fishingCtx, LootContextJS unknownCtx) {
        helper.debugStack.h2("Killer entity detection by type");

        helper.shouldSucceed(entityCtx.getKillerEntity() != null, "Entity context has killer entity");
        helper.shouldSucceed(blockCtx.getKillerEntity() == null, "Block context has no killer entity");
        helper.shouldSucceed(chestCtx.getKillerEntity() == null, "Chest context without killer entity");
        helper.shouldSucceed(
                fishingCtx.getKillerEntity() != null && fishingCtx.getKillerEntity().minecraftEntity == helper.player,
                "Fishing context has killer entity");
        helper.shouldSucceed(unknownCtx.getKillerEntity() == null, "Unknown context without killer entity");
    }

    private static void blockDetection(TestHelper helper, LootContextJS entityCtx, LootContextJS blockCtx, LootContextJS chestCtx, LootContextJS fishingCtx, LootContextJS unknownCtx, BlockState blockState) {
        helper.debugStack.h2("Block detection by type");

        helper.shouldSucceed(entityCtx.getDestroyedBlock() == null, "Entity context has no block");
        helper.shouldSucceed(
                blockCtx.getDestroyedBlock() != null && blockCtx.getDestroyedBlock().getBlockState() == blockState,
                "Block context has a block");
        helper.shouldSucceed(chestCtx.getDestroyedBlock() == null, "Chest context has no block");
        helper.shouldSucceed(fishingCtx.getDestroyedBlock() == null, "Fishing context has no block");
        helper.shouldSucceed(unknownCtx.getDestroyedBlock() == null, "Unknown context has no block");
    }

    private static void entityDetection(TestHelper helper, LootContextJS entityCtx, LootContextJS blockCtx, LootContextJS chestCtx, LootContextJS fishingCtx, LootContextJS unknownCtx) {
        helper.debugStack.h2("Entity detection by type");

        LootContextJS blockCtxWithoutPlayer = new LootContextJS(helper.blockContext(helper.player
                .blockPosition()
                .below(), false));
        LootContextJS chestCtxWithoutPlayer = new LootContextJS(helper.chestContext(helper.player.position(), false));

        helper.shouldSucceed(
                entityCtx.getEntity() != null && entityCtx.getEntity().minecraftEntity.getType() == EntityType.CREEPER,
                "Entity context has entity");
        helper.shouldSucceed(blockCtx.getEntity() != null && blockCtx.getEntity().minecraftEntity == helper.player,
                "Block context has entity");
        helper.shouldSucceed(chestCtx.getEntity() != null && chestCtx.getEntity().minecraftEntity == helper.player,
                "Chest context has entity");
        helper.shouldSucceed(blockCtxWithoutPlayer.getEntity() == null, "Block context without player has no entity");
        helper.shouldSucceed(chestCtxWithoutPlayer.getEntity() == null, "Chest context without player has no entity");
        helper.shouldSucceed(fishingCtx.getEntity() != null &&
                             fishingCtx.getEntity().minecraftEntity.getType() == EntityType.FISHING_BOBBER,
                "Fishing context has entity");
        helper.shouldSucceed(unknownCtx.getEntity() == null, "Unknown context without entity");
    }

    private static void playerDetection(TestHelper helper, LootContextJS entityCtx, LootContextJS blockCtx, LootContextJS chestCtx, LootContextJS fishingCtx, LootContextJS unknownCtx) {
        helper.debugStack.h2("Player detection by type");

        helper.shouldSucceed(entityCtx.getPlayer() != null && entityCtx.getPlayer().minecraftEntity == helper.player,
                "Correct player for entity context");
        helper.shouldSucceed(blockCtx.getPlayer() != null && blockCtx.getPlayer().minecraftEntity == helper.player,
                "Correct player for block context");
        helper.shouldSucceed(chestCtx.getPlayer() != null && chestCtx.getPlayer().minecraftEntity == helper.player,
                "Correct player for chest context");
        helper.shouldSucceed(fishingCtx.getPlayer() != null && fishingCtx.getPlayer().minecraftEntity == helper.player,
                "Correct player for fishing context");
        helper.shouldSucceed(unknownCtx.getPlayer() == null, "No player for unknown context");

        CreeperEntity creeper = helper.simpleEntity(EntityType.CREEPER, new BlockPos(helper.player.position()));
        SkeletonEntity skeleton = helper.simpleEntity(EntityType.SKELETON, new BlockPos(helper.player.position()));
        LootContextJS ctxKilledBySkeleton = new LootContextJS(creeper
                .createLootContext(true, DamageSource.mobAttack(skeleton))
                .create(LootParameterSets.ENTITY));
        helper.yeet(creeper);
        helper.yeet(skeleton);
        helper.shouldSucceed(ctxKilledBySkeleton.getPlayer() == null, "No player. Killed by skeleton");
    }

}
