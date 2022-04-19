package com.github.llytho.lootjs_test.tests;

import com.github.llytho.lootjs.kube.builder.DamageSourcePredicateBuilderJS;
import com.github.llytho.lootjs.loot.condition.WrappedDamageSourceCondition;
import com.github.llytho.lootjs.predicate.ExtendedEntityFlagsPredicate;
import com.github.llytho.lootjs_test.AllTests;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.function.Function;
import java.util.function.Supplier;

public class BuilderTests {
    public static void loadTests() {
        AllTests.add("DamageSourcePredicateBuilderJS", helper -> {
            helper.debugStack.pushLayer();
            Supplier<DamageSourcePredicateBuilderJS> dsb = DamageSourcePredicateBuilderJS::new;
            Function<DamageSourcePredicateBuilderJS, JsonElement> toJson = (builder) -> builder
                    .build()
                    .serializeToJson();

            helper.debugStack.h2("NULL checks for default values");
            WrappedDamageSourceCondition defaultP = new DamageSourcePredicateBuilderJS().build();
            JsonObject jsonObject = defaultP.serializeToJson();
            JsonObject dsp = jsonObject.get("DamageSourcePredicate").getAsJsonObject();
            helper.shouldBeNull(dsp, "is_projectile");
            helper.shouldBeNull(dsp, "is_explosion");
            helper.shouldBeNull(dsp, "bypasses_armor");
            helper.shouldBeNull(dsp, "bypasses_invulnerability");
            helper.shouldBeNull(dsp, "bypasses_magic");
            helper.shouldBeNull(dsp, "is_fire");
            helper.shouldBeNull(dsp, "is_magic");
            helper.shouldBeNull(dsp, "is_lightning");

            helper.debugStack.h2("TRUE checks");
            helper.shouldBeTrue(toJson.apply(dsb.get().isProjectile(true)), "DamageSourcePredicate/is_projectile");
            helper.shouldBeTrue(toJson.apply(dsb.get().isExplosion(true)), "DamageSourcePredicate/is_explosion");
            helper.shouldBeTrue(toJson.apply(dsb.get().doesBypassArmor(true)), "DamageSourcePredicate/bypasses_armor");
            helper.shouldBeTrue(toJson.apply(dsb.get().doesBypassInvulnerability(true)),
                    "DamageSourcePredicate/bypasses_invulnerability");
            helper.shouldBeTrue(toJson.apply(dsb.get().doesBypassMagic(true)), "DamageSourcePredicate/bypasses_magic");
            helper.shouldBeTrue(toJson.apply(dsb.get().isFire(true)), "DamageSourcePredicate/is_fire");
            helper.shouldBeTrue(toJson.apply(dsb.get().isMagic(true)), "DamageSourcePredicate/is_magic");
            helper.shouldBeTrue(toJson.apply(dsb.get().isLightning(true)), "DamageSourcePredicate/is_lightning");

            helper.debugStack.h2("FALSE checks");
            helper.shouldBeFalse(toJson.apply(dsb.get().isProjectile(false)), "DamageSourcePredicate/is_projectile");
            helper.shouldBeFalse(toJson.apply(dsb.get().isExplosion(false)), "DamageSourcePredicate/is_explosion");
            helper.shouldBeFalse(toJson.apply(dsb.get().doesBypassArmor(false)),
                    "DamageSourcePredicate/bypasses_armor");
            helper.shouldBeFalse(toJson.apply(dsb.get().doesBypassInvulnerability(false)),
                    "DamageSourcePredicate/bypasses_invulnerability");
            helper.shouldBeFalse(toJson.apply(dsb.get().doesBypassMagic(false)),
                    "DamageSourcePredicate/bypasses_magic");
            helper.shouldBeFalse(toJson.apply(dsb.get().isFire(false)), "DamageSourcePredicate/is_fire");
            helper.shouldBeFalse(toJson.apply(dsb.get().isMagic(false)), "DamageSourcePredicate/is_magic");
            helper.shouldBeFalse(toJson.apply(dsb.get().isLightning(false)), "DamageSourcePredicate/is_lightning");

            helper.debugStack.popLayer();
        });

        AllTests.add("ExtendedEntityFlagsPredicate.Builder", helper -> {
            helper.debugStack.pushLayer();
            Supplier<ExtendedEntityFlagsPredicate.Builder> fb = ExtendedEntityFlagsPredicate.Builder::new;
            Function<ExtendedEntityFlagsPredicate.Builder, JsonElement> toJson = (builder) -> builder
                    .build()
                    .serializeToJson();

            helper.debugStack.h2("NULL checks for default values");
            ExtendedEntityFlagsPredicate defFlags = new ExtendedEntityFlagsPredicate.Builder().build();
            JsonElement jsonElement = defFlags.serializeToJson();
            helper.shouldBeNull(jsonElement, "is_on_fire");
            helper.shouldBeNull(jsonElement, "is_sneaking");
            helper.shouldBeNull(jsonElement, "is_sprinting");
            helper.shouldBeNull(jsonElement, "is_swimming");
            helper.shouldBeNull(jsonElement, "is_baby");
            helper.shouldBeNull(jsonElement, "isInWater");
            helper.shouldBeNull(jsonElement, "isUnderWater");
            helper.shouldBeNull(jsonElement, "isMonster");
            helper.shouldBeNull(jsonElement, "isCreature");
            helper.shouldBeNull(jsonElement, "isOnGround");
            helper.shouldBeNull(jsonElement, "isUndeadMob");
            helper.shouldBeNull(jsonElement, "isArthropodMob");
            helper.shouldBeNull(jsonElement, "isIllegarMob");
            helper.shouldBeNull(jsonElement, "isWaterMob");

            helper.debugStack.h2("TRUE checks");
            helper.shouldBeTrue(toJson.apply(fb.get().isOnFire(true)), "is_on_fire");
            helper.shouldBeTrue(toJson.apply(fb.get().isCrouching(true)), "is_sneaking");
            helper.shouldBeTrue(toJson.apply(fb.get().isSprinting(true)), "is_sprinting");
            helper.shouldBeTrue(toJson.apply(fb.get().isSwimming(true)), "is_swimming");
            helper.shouldBeTrue(toJson.apply(fb.get().isBaby(true)), "is_baby");
            helper.shouldBeTrue(toJson.apply(fb.get().isInWater(true)), "isInWater");
            helper.shouldBeTrue(toJson.apply(fb.get().isUnderWater(true)), "isUnderWater");
            helper.shouldBeTrue(toJson.apply(fb.get().isMonster(true)), "isMonster");
            helper.shouldBeTrue(toJson.apply(fb.get().isCreature(true)), "isCreature");
            helper.shouldBeTrue(toJson.apply(fb.get().isOnGround(true)), "isOnGround");
            helper.shouldBeTrue(toJson.apply(fb.get().isUndeadMob(true)), "isUndeadMob");
            helper.shouldBeTrue(toJson.apply(fb.get().isArthropodMob(true)), "isArthropodMob");
            helper.shouldBeTrue(toJson.apply(fb.get().isIllegarMob(true)), "isIllegarMob");
            helper.shouldBeTrue(toJson.apply(fb.get().isWaterMob(true)), "isWaterMob");

            helper.debugStack.h2("FALSE checks");
            helper.shouldBeFalse(toJson.apply(fb.get().isOnFire(false)), "is_on_fire");
            helper.shouldBeFalse(toJson.apply(fb.get().isCrouching(false)), "is_sneaking");
            helper.shouldBeFalse(toJson.apply(fb.get().isSprinting(false)), "is_sprinting");
            helper.shouldBeFalse(toJson.apply(fb.get().isSwimming(false)), "is_swimming");
            helper.shouldBeFalse(toJson.apply(fb.get().isBaby(false)), "is_baby");
            helper.shouldBeFalse(toJson.apply(fb.get().isInWater(false)), "isInWater");
            helper.shouldBeFalse(toJson.apply(fb.get().isUnderWater(false)), "isUnderWater");
            helper.shouldBeFalse(toJson.apply(fb.get().isMonster(false)), "isMonster");
            helper.shouldBeFalse(toJson.apply(fb.get().isCreature(false)), "isCreature");
            helper.shouldBeFalse(toJson.apply(fb.get().isOnGround(false)), "isOnGround");
            helper.shouldBeFalse(toJson.apply(fb.get().isUndeadMob(false)), "isUndeadMob");
            helper.shouldBeFalse(toJson.apply(fb.get().isArthropodMob(false)), "isArthropodMob");
            helper.shouldBeFalse(toJson.apply(fb.get().isIllegarMob(false)), "isIllegarMob");
            helper.shouldBeFalse(toJson.apply(fb.get().isWaterMob(false)), "isWaterMob");

            helper.debugStack.popLayer();
        });
    }

}
