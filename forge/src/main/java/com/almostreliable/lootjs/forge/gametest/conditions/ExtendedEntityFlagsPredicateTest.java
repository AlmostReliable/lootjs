package com.almostreliable.lootjs.forge.gametest.conditions;

import com.almostreliable.lootjs.BuildConfig;
import com.almostreliable.lootjs.forge.gametest.GameTestTemplates;
import com.almostreliable.lootjs.forge.gametest.GameTestUtils;
import com.almostreliable.lootjs.predicate.ExtendedEntityFlagsPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(value = BuildConfig.MOD_ID)
@PrefixGameTestTemplate(false)
public class ExtendedEntityFlagsPredicateTest {
    private static final BlockPos TEST_POS = new BlockPos(0, 0, 0);

    @GameTest(template = GameTestTemplates.EMPTY)
    public void isMonsterTest(GameTestHelper helper) {
        Entity entity = GameTestUtils.simpleEntity(EntityType.COW, helper.getLevel(), TEST_POS);
        helper.succeedIf(() -> {
            ExtendedEntityFlagsPredicate fFlag = new ExtendedEntityFlagsPredicate.Builder().isMonster(false).build();
            GameTestUtils.assertTrue(helper, fFlag.matches(entity));
            ExtendedEntityFlagsPredicate tFlag = new ExtendedEntityFlagsPredicate.Builder().isMonster(true).build();
            GameTestUtils.assertFalse(helper, tFlag.matches(entity));
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void isCreatureTest(GameTestHelper helper) {
        Entity entity = GameTestUtils.simpleEntity(EntityType.COW, helper.getLevel(), TEST_POS);
        helper.succeedIf(() -> {
            ExtendedEntityFlagsPredicate fFlag = new ExtendedEntityFlagsPredicate.Builder().isCreature(false).build();
            GameTestUtils.assertFalse(helper, fFlag.matches(entity));
            ExtendedEntityFlagsPredicate tFlag = new ExtendedEntityFlagsPredicate.Builder().isCreature(true).build();
            GameTestUtils.assertTrue(helper, tFlag.matches(entity));
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void isUndeadTest(GameTestHelper helper) {
        Entity entity = GameTestUtils.simpleEntity(EntityType.ZOMBIE, helper.getLevel(), TEST_POS);
        helper.succeedIf(() -> {
            ExtendedEntityFlagsPredicate fFlag = new ExtendedEntityFlagsPredicate.Builder().isUndeadMob(false).build();
            GameTestUtils.assertFalse(helper, fFlag.matches(entity));
            ExtendedEntityFlagsPredicate tFlag = new ExtendedEntityFlagsPredicate.Builder().isUndeadMob(true).build();
            GameTestUtils.assertTrue(helper, tFlag.matches(entity));
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void isArthropodTest(GameTestHelper helper) {
        Entity entity = GameTestUtils.simpleEntity(EntityType.SPIDER, helper.getLevel(), TEST_POS);
        helper.succeedIf(() -> {
            ExtendedEntityFlagsPredicate fFlag = new ExtendedEntityFlagsPredicate.Builder()
                    .isArthropodMob(false)
                    .build();
            GameTestUtils.assertFalse(helper, fFlag.matches(entity));
            ExtendedEntityFlagsPredicate tFlag = new ExtendedEntityFlagsPredicate.Builder()
                    .isArthropodMob(true)
                    .build();
            GameTestUtils.assertTrue(helper, tFlag.matches(entity));
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void isIllegarTest(GameTestHelper helper) {
        Entity entity = GameTestUtils.simpleEntity(EntityType.PILLAGER, helper.getLevel(), TEST_POS);
        helper.succeedIf(() -> {
            ExtendedEntityFlagsPredicate fFlag = new ExtendedEntityFlagsPredicate.Builder().isIllegarMob(false).build();
            GameTestUtils.assertFalse(helper, fFlag.matches(entity));
            ExtendedEntityFlagsPredicate tFlag = new ExtendedEntityFlagsPredicate.Builder().isIllegarMob(true).build();
            GameTestUtils.assertTrue(helper, tFlag.matches(entity));
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void isWaterMobTest(GameTestHelper helper) {
        Entity entity = GameTestUtils.simpleEntity(EntityType.TURTLE, helper.getLevel(), TEST_POS);
        helper.succeedIf(() -> {
            ExtendedEntityFlagsPredicate fFlag = new ExtendedEntityFlagsPredicate.Builder().isWaterMob(false).build();
            GameTestUtils.assertFalse(helper, fFlag.matches(entity));
            ExtendedEntityFlagsPredicate tFlag = new ExtendedEntityFlagsPredicate.Builder().isWaterMob(true).build();
            GameTestUtils.assertTrue(helper, tFlag.matches(entity));
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void onGroundTest(GameTestHelper helper) {
        Entity entity = GameTestUtils.simpleEntity(EntityType.COW, helper.getLevel(), TEST_POS);
        ExtendedEntityFlagsPredicate flags = new ExtendedEntityFlagsPredicate.Builder().isOnGround(true).build();
        helper.succeedIf(() -> {
            entity.setOnGround(true);
            GameTestUtils.assertTrue(helper, flags.matches(entity));
            entity.setOnGround(false);
            GameTestUtils.assertFalse(helper, flags.matches(entity));
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void inWaterTest(GameTestHelper helper) {
        Entity entity = GameTestUtils.simpleEntity(EntityType.COW, helper.getLevel(), TEST_POS);
        ExtendedEntityFlagsPredicate flags = new ExtendedEntityFlagsPredicate.Builder().isInWater(true).build();
        helper.succeedIf(() -> {
            entity.wasTouchingWater = false;
            GameTestUtils.assertFalse(helper, flags.matches(entity));
            entity.wasTouchingWater = true;
            GameTestUtils.assertTrue(helper, flags.matches(entity));
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void inUnderWaterTest(GameTestHelper helper) {
        Entity entity = GameTestUtils.simpleEntity(EntityType.COW, helper.getLevel(), TEST_POS);
        ExtendedEntityFlagsPredicate flags = new ExtendedEntityFlagsPredicate.Builder().isUnderWater(true).build();
        helper.succeedIf(() -> {
            entity.wasTouchingWater = false;
            entity.wasEyeInWater = false;
            GameTestUtils.assertFalse(helper, flags.matches(entity));
            entity.wasTouchingWater = true;
            entity.wasEyeInWater = true;
            GameTestUtils.assertTrue(helper, flags.matches(entity));
        });
    }
}
