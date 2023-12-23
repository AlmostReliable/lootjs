package com.almostreliable.lootjs.forge.gametest;

import com.almostreliable.lootjs.BuildConfig;
import com.almostreliable.lootjs.predicate.ExtendedEntityFlagsPredicate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import java.util.function.BiConsumer;

@GameTestHolder(value = BuildConfig.MOD_ID)
@PrefixGameTestTemplate(false)
public class BuilderTests {

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate(GameTestHelper helper) {
        helper.succeedIf(() -> {
            ExtendedEntityFlagsPredicate defFlags = new ExtendedEntityFlagsPredicate.Builder().build();
            JsonElement jsonElement = defFlags.serializeToJson();
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("is_on_fire"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("is_sneaking"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("is_sprinting"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("is_swimming"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("is_baby"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("isInWater"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("isUnderWater"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("isMonster"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("isCreature"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("isOnGround"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("isUndeadMob"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("isArthropodMob"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("isIllegarMob"));
            GameTestUtils.assertNull(helper, jsonElement.getAsJsonObject().get("isWaterMob"));
        });
    }

    public void extendedEntityFlagsPredicateFieldTest(GameTestHelper helper, BiConsumer<ExtendedEntityFlagsPredicate.Builder, Boolean> setter, String key, boolean value) {
        helper.succeedIf(() -> {
            ExtendedEntityFlagsPredicate.Builder builder = new ExtendedEntityFlagsPredicate.Builder();
            setter.accept(builder, value);
            JsonObject eef = builder.build().serializeToJson().getAsJsonObject();
            GameTestUtils.assertEquals(helper, eef.get(key).getAsBoolean(), value);
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isOnFire(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isOnFire,
                "is_on_fire",
                true);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isCrouching(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isCrouching,
                "is_sneaking",
                true);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isSprinting(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isSprinting,
                "is_sprinting",
                true);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isSwimming(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isSwimming,
                "is_swimming",
                true);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isBaby(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isBaby,
                "is_baby",
                true);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isInWater(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isInWater,
                "isInWater",
                true);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isUnderWater(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isUnderWater,
                "isUnderWater",
                true);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isMonster(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isMonster,
                "isMonster",
                true);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isCreature(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isCreature,
                "isCreature",
                true);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isOnGround(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isOnGround,
                "isOnGround",
                true);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isUndeadMob(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isUndeadMob,
                "isUndeadMob",
                true);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isArthropodMob(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isArthropodMob,
                "isArthropodMob",
                true);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isIllegarMob(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isIllegarMob,
                "isIllegarMob",
                true);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isWaterMob(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isWaterMob,
                "isWaterMob",
                true);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isOnFire_false(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isOnFire,
                "is_on_fire",
                true);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isCrouching_false(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isCrouching,
                "is_sneaking",
                false);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isSprinting_false(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isSprinting,
                "is_sprinting",
                false);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isSwimming_false(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isSwimming,
                "is_swimming",
                false);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isBaby_false(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isBaby,
                "is_baby",
                false);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isInWater_false(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isInWater,
                "isInWater",
                false);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isUnderWater_false(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isUnderWater,
                "isUnderWater",
                false);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isMonster_false(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isMonster,
                "isMonster",
                false);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isCreature_false(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isCreature,
                "isCreature",
                false);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isOnGround_false(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isOnGround,
                "isOnGround",
                false);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isUndeadMob_false(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isUndeadMob,
                "isUndeadMob",
                false);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isArthropodMob_false(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isArthropodMob,
                "isArthropodMob",
                false);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isIllegarMob_false(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isIllegarMob,
                "isIllegarMob",
                false);
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void extendedEntityFlagsPredicate_isWaterMob_false(GameTestHelper helper) {
        extendedEntityFlagsPredicateFieldTest(helper,
                ExtendedEntityFlagsPredicate.Builder::isWaterMob,
                "isWaterMob",
                false);
    }
}
