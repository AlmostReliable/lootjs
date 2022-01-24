package com.github.llytho.lootjs_test.tests;

import com.github.llytho.lootjs.kube.builder.DamageSourcePredicateBuilderJS;
import com.github.llytho.lootjs.loot.condition.WrappedDamageSourceCondition;
import com.github.llytho.lootjs.predicate.ExtendedEntityFlagsPredicate;
import com.github.llytho.lootjs_test.AllTests;

import java.util.function.Supplier;

public class BuilderTests {
    public static void loadTests() {
        AllTests.add("DamageSourcePredicateBuilderJS", helper -> {
            helper.debugStack.pushLayer();
            Supplier<DamageSourcePredicateBuilderJS> dsb = DamageSourcePredicateBuilderJS::new;

            helper.debugStack.h2("NULL checks for default values");
            WrappedDamageSourceCondition defaultP = new DamageSourcePredicateBuilderJS().build();
            helper.shouldBeNull(defaultP, "predicate/isProjectile");
            helper.shouldBeNull(defaultP, "predicate/isExplosion");
            helper.shouldBeNull(defaultP, "predicate/bypassesArmor");
            helper.shouldBeNull(defaultP, "predicate/bypassesInvulnerability");
            helper.shouldBeNull(defaultP, "predicate/bypassesMagic");
            helper.shouldBeNull(defaultP, "predicate/isFire");
            helper.shouldBeNull(defaultP, "predicate/isMagic");
            helper.shouldBeNull(defaultP, "predicate/isLightning");

            helper.debugStack.h2("TRUE checks");
            helper.shouldBeTrue(dsb.get().isProjectile(true).build(), "predicate/isProjectile");
            helper.shouldBeTrue(dsb.get().isExplosion(true).build(), "predicate/isExplosion");
            helper.shouldBeTrue(dsb.get().doesBypassArmor(true).build(), "predicate/bypassesArmor");
            helper.shouldBeTrue(dsb.get().doesBypassInvulnerability(true).build(), "predicate/bypassesInvulnerability");
            helper.shouldBeTrue(dsb.get().doesBypassMagic(true).build(), "predicate/bypassesMagic");
            helper.shouldBeTrue(dsb.get().isFire(true).build(), "predicate/isFire");
            helper.shouldBeTrue(dsb.get().isMagic(true).build(), "predicate/isMagic");
            helper.shouldBeTrue(dsb.get().isLightning(true).build(), "predicate/isLightning");

            helper.debugStack.h2("FALSE checks");
            helper.shouldBeFalse(dsb.get().isProjectile(false).build(), "predicate/isProjectile");
            helper.shouldBeFalse(dsb.get().isExplosion(false).build(), "predicate/isExplosion");
            helper.shouldBeFalse(dsb.get().doesBypassArmor(false).build(), "predicate/bypassesArmor");
            helper.shouldBeFalse(dsb.get().doesBypassInvulnerability(false).build(),
                    "predicate/bypassesInvulnerability");
            helper.shouldBeFalse(dsb.get().doesBypassMagic(false).build(), "predicate/bypassesMagic");
            helper.shouldBeFalse(dsb.get().isFire(false).build(), "predicate/isFire");
            helper.shouldBeFalse(dsb.get().isMagic(false).build(), "predicate/isMagic");
            helper.shouldBeFalse(dsb.get().isLightning(false).build(), "predicate/isLightning");

            helper.debugStack.popLayer();
        });

        AllTests.add("ExtendedEntityFlagsPredicate.Builder", helper -> {
            helper.debugStack.pushLayer();
            Supplier<ExtendedEntityFlagsPredicate.Builder> fb = ExtendedEntityFlagsPredicate.Builder::new;

            helper.debugStack.h2("NULL checks for default values");
            ExtendedEntityFlagsPredicate defFlags = new ExtendedEntityFlagsPredicate.Builder().build();
            helper.shouldBeNull(defFlags, "isOnFire");
            helper.shouldBeNull(defFlags, "isCrouching");
            helper.shouldBeNull(defFlags, "isSprinting");
            helper.shouldBeNull(defFlags, "isSwimming");
            helper.shouldBeNull(defFlags, "isBaby");
            helper.shouldBeNull(defFlags, "isInWater");
            helper.shouldBeNull(defFlags, "isUnderWater");
            helper.shouldBeNull(defFlags, "isMonster");
            helper.shouldBeNull(defFlags, "isCreature");
            helper.shouldBeNull(defFlags, "isOnGround");
            helper.shouldBeNull(defFlags, "isUndeadMob");
            helper.shouldBeNull(defFlags, "isArthropodMob");
            helper.shouldBeNull(defFlags, "isIllegarMob");
            helper.shouldBeNull(defFlags, "isWaterMob");

            helper.debugStack.h2("TRUE checks");
            helper.shouldBeTrue(fb.get().isOnFire(true).build(), "isOnFire");
            helper.shouldBeTrue(fb.get().isCrouching(true).build(), "isCrouching");
            helper.shouldBeTrue(fb.get().isSprinting(true).build(), "isSprinting");
            helper.shouldBeTrue(fb.get().isSwimming(true).build(), "isSwimming");
            helper.shouldBeTrue(fb.get().isBaby(true).build(), "isBaby");
            helper.shouldBeTrue(fb.get().isInWater(true).build(), "isInWater");
            helper.shouldBeTrue(fb.get().isUnderWater(true).build(), "isUnderWater");
            helper.shouldBeTrue(fb.get().isMonster(true).build(), "isMonster");
            helper.shouldBeTrue(fb.get().isCreature(true).build(), "isCreature");
            helper.shouldBeTrue(fb.get().isOnGround(true).build(), "isOnGround");
            helper.shouldBeTrue(fb.get().isUndeadMob(true).build(), "isUndeadMob");
            helper.shouldBeTrue(fb.get().isArthropodMob(true).build(), "isArthropodMob");
            helper.shouldBeTrue(fb.get().isIllegarMob(true).build(), "isIllegarMob");
            helper.shouldBeTrue(fb.get().isWaterMob(true).build(), "isWaterMob");

            helper.debugStack.h2("FALSE checks");
            helper.shouldBeFalse(fb.get().isOnFire(false).build(), "isOnFire");
            helper.shouldBeFalse(fb.get().isCrouching(false).build(), "isCrouching");
            helper.shouldBeFalse(fb.get().isSprinting(false).build(), "isSprinting");
            helper.shouldBeFalse(fb.get().isSwimming(false).build(), "isSwimming");
            helper.shouldBeFalse(fb.get().isBaby(false).build(), "isBaby");
            helper.shouldBeFalse(fb.get().isInWater(false).build(), "isInWater");
            helper.shouldBeFalse(fb.get().isUnderWater(false).build(), "isUnderWater");
            helper.shouldBeFalse(fb.get().isMonster(false).build(), "isMonster");
            helper.shouldBeFalse(fb.get().isCreature(false).build(), "isCreature");
            helper.shouldBeFalse(fb.get().isOnGround(false).build(), "isOnGround");
            helper.shouldBeFalse(fb.get().isUndeadMob(false).build(), "isUndeadMob");
            helper.shouldBeFalse(fb.get().isArthropodMob(false).build(), "isArthropodMob");
            helper.shouldBeFalse(fb.get().isIllegarMob(false).build(), "isIllegarMob");
            helper.shouldBeFalse(fb.get().isWaterMob(false).build(), "isWaterMob");

            helper.debugStack.popLayer();
        });
    }

}
