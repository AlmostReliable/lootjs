package com.github.llytho.lootjs_test.tests;

import com.github.llytho.lootjs.predicate.ExtendedEntityFlagsPredicate;
import com.github.llytho.lootjs_test.AllTests;
import net.minecraft.world.entity.EntityType;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.TurtleEntity;

import java.util.function.Supplier;

public class PredicateTests {
    public static void loadTests() {
        AllTests.add("ExtendedEntityFlagsPredicate", helper -> {
            helper.debugStack.pushLayer();

            Supplier<ExtendedEntityFlagsPredicate.Builder> fb = ExtendedEntityFlagsPredicate.Builder::new;

            CowEntity cow = helper.simpleEntity(EntityType.COW, helper.player.blockPosition());
            SpiderEntity spider = helper.simpleEntity(EntityType.SPIDER, helper.player.blockPosition());
            SkeletonEntity skeleton = helper.simpleEntity(EntityType.SKELETON, helper.player.blockPosition());
            PillagerEntity pillager = helper.simpleEntity(EntityType.PILLAGER, helper.player.blockPosition());
            TurtleEntity turtle = helper.simpleEntity(EntityType.TURTLE, helper.player.blockPosition());

            cow.setOnGround(true);
            spider.setOnGround(false);
            skeleton.wasEyeInWater = true; // under water check
            skeleton.wasTouchingWater = true; // in water check. Underwater => wasEyeInWater && wasTouchingWater
            pillager.wasTouchingWater = true; // in water check

            helper.debugStack.h2("isMonster");
            ExtendedEntityFlagsPredicate flags = fb.get().isMonster(true).build();
            helper.shouldFail(flags.matches(cow), "[true] Cow is not a monster");
            helper.shouldSucceed(flags.matches(spider), "[true] Spider is a monster");
            helper.shouldSucceed(flags.matches(skeleton), "[true] Skeleton is a monster");
            helper.shouldSucceed(flags.matches(pillager), "[true] Pillager is a monster");

            flags = fb.get().isMonster(false).build();
            helper.shouldSucceed(flags.matches(cow), "[false] Cow is not a monster");
            helper.shouldFail(flags.matches(spider), "[false] Spider is a monster");
            helper.shouldFail(flags.matches(skeleton), "[false] Skeleton is a monster");
            helper.shouldFail(flags.matches(pillager), "[false] Pillager is a monster");

            helper.debugStack.h2("isCreature");
            flags = fb.get().isCreature(true).build();
            helper.shouldSucceed(flags.matches(cow), "[true] Cow is a creature");
            helper.shouldFail(flags.matches(spider), "[true] Spider is not a creature");
            helper.shouldFail(flags.matches(skeleton), "[true] Skeleton is not a creature");
            helper.shouldFail(flags.matches(pillager), "[true] Pillager is not a creature");

            flags = fb.get().isCreature(false).build();
            helper.shouldFail(flags.matches(cow), "[false] Cow is a creature");
            helper.shouldSucceed(flags.matches(spider), "[false] Spider is not a creature");
            helper.shouldSucceed(flags.matches(skeleton), "[false] Skeleton is not a creature");
            helper.shouldSucceed(flags.matches(pillager), "[false] Pillager is not a creature");

            helper.debugStack.h2("isOnGround");
            flags = fb.get().isOnGround(true).build();
            helper.shouldSucceed(flags.matches(cow), "[true] Cow is on ground");
            helper.shouldFail(flags.matches(spider), "[true] Spider is in air");

            flags = fb.get().isOnGround(false).build();
            helper.shouldFail(flags.matches(cow), "[false] Cow is on ground");
            helper.shouldSucceed(flags.matches(spider), "[false] Spider is in air");

            helper.debugStack.h2("isInWater");
            flags = fb.get().isInWater(true).build();
            helper.shouldFail(flags.matches(cow), "[true] Cow is not in water or below");
            helper.shouldSucceed(flags.matches(skeleton), "[true] Skeleton is below water");
            helper.shouldSucceed(flags.matches(pillager), "[true] Pillager is in water, not below");

            flags = fb.get().isInWater(false).build();
            helper.shouldSucceed(flags.matches(cow), "[false] Cow is not in water or below");
            helper.shouldFail(flags.matches(skeleton), "[false] Skeleton is below water");
            helper.shouldFail(flags.matches(pillager), "[false] Pillager is in water, not below");

            helper.debugStack.h2("isUnderWater");
            flags = fb.get().isUnderWater(true).build();
            helper.shouldFail(flags.matches(cow), "[true] Cow is not in water or below");
            helper.shouldSucceed(flags.matches(skeleton), "[true] Skeleton is below water");
            helper.shouldFail(flags.matches(pillager), "[true] Pillager is in water, not below");

            flags = fb.get().isUnderWater(false).build();
            helper.shouldSucceed(flags.matches(cow), "[false] Cow is not in water or below");
            helper.shouldFail(flags.matches(skeleton), "[false] Skeleton is below water");
            helper.shouldSucceed(flags.matches(pillager), "[false] Pillager is in water, not below");

            helper.debugStack.h2("isUndeadMob");
            flags = fb.get().isUndeadMob(true).build();
            helper.shouldFail(flags.matches(cow), "[true] Cow is not undead");
            helper.shouldFail(flags.matches(spider), "[true] Spider is not undead");
            helper.shouldSucceed(flags.matches(skeleton), "[true] Skeleton is undead");
            helper.shouldFail(flags.matches(pillager), "[true] Pillager is not undead");

            flags = fb.get().isUndeadMob(false).build();
            helper.shouldSucceed(flags.matches(cow), "[false] Cow is not undead");
            helper.shouldSucceed(flags.matches(spider), "[false] Spider is not undead");
            helper.shouldFail(flags.matches(skeleton), "[false] Skeleton is undead");
            helper.shouldSucceed(flags.matches(pillager), "[false] Pillager is not undead");

            helper.debugStack.h2("isArthropodMob");
            flags = fb.get().isArthropodMob(true).build();
            helper.shouldFail(flags.matches(cow), "[true] Cow is not arthropod");
            helper.shouldSucceed(flags.matches(spider), "[true] Spider is arthropod");
            helper.shouldFail(flags.matches(skeleton), "[true] Skeleton is not arthropod");
            helper.shouldFail(flags.matches(pillager), "[true] Pillager is not arthropod");

            flags = fb.get().isArthropodMob(false).build();
            helper.shouldSucceed(flags.matches(cow), "[false] Cow is not arthropod");
            helper.shouldFail(flags.matches(spider), "[false] Spider is arthropod");
            helper.shouldSucceed(flags.matches(skeleton), "[false] Skeleton is not arthropod");
            helper.shouldSucceed(flags.matches(pillager), "[false] Pillager is not arthropod");

            helper.debugStack.h2("isIllegarMob");
            flags = fb.get().isIllegarMob(true).build();
            helper.shouldFail(flags.matches(cow), "[true] Cow is not an illegar");
            helper.shouldFail(flags.matches(spider), "[true] Spider is not an illegar");
            helper.shouldFail(flags.matches(skeleton), "[true] Skeleton is not an illegar");
            helper.shouldSucceed(flags.matches(pillager), "[true] Pillager is an illegar");

            flags = fb.get().isIllegarMob(false).build();
            helper.shouldSucceed(flags.matches(cow), "[false] Cow is not an illegar");
            helper.shouldSucceed(flags.matches(spider), "[false] Spider is not an illegar");
            helper.shouldSucceed(flags.matches(skeleton), "[false] Skeleton is not an illegar");
            helper.shouldFail(flags.matches(pillager), "[false] Pillager is an illegar");

            helper.debugStack.h2("isWaterMob");
            flags = fb.get().isWaterMob(true).build();
            helper.shouldFail(flags.matches(cow), "[true] Cow is not a water mob");
            helper.shouldSucceed(flags.matches(turtle), "[true] Turtle is a water mob");

            flags = fb.get().isWaterMob(false).build();
            helper.shouldSucceed(flags.matches(cow), "[false] Cow is not a water mob");
            helper.shouldFail(flags.matches(turtle), "[false] Turtle is a water mob");

            helper.yeet(cow);
            helper.yeet(spider);
            helper.yeet(skeleton);
            helper.yeet(pillager);
            helper.yeet(turtle);

            helper.debugStack.popLayer();
        });
    }
}
