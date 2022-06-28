package com.almostreliable.lootjs.forge.gametest.conditions;

import com.almostreliable.lootjs.BuildConfig;
import com.almostreliable.lootjs.forge.gametest.GameTestTemplates;
import com.almostreliable.lootjs.forge.gametest.GameTestUtils;
import com.almostreliable.lootjs.loot.condition.MatchKillerDistance;
import com.almostreliable.lootjs.loot.condition.builder.DistancePredicateBuilder;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(value = BuildConfig.MOD_ID)
@PrefixGameTestTemplate(false)
public class MatchKillerDistanceTest {
    private static final BlockPos TEST_POS = new BlockPos(0, 0, 0);

    @GameTest(template = GameTestTemplates.EMPTY)
    public void matchDistance(GameTestHelper helper) {
        Player player = helper.makeMockPlayer();
        Cow cow = GameTestUtils.simpleEntity(EntityType.COW, helper.getLevel(), TEST_POS);
        LootContext ctx = cow
                .createLootContext(true, DamageSource.playerAttack(player))
                .create(LootContextParamSets.ENTITY);

        double dist = player.distanceTo(cow);
        MatchKillerDistance mkd = new MatchKillerDistance(new DistancePredicateBuilder()
                .absolute(new MinMaxBounds.Doubles(dist - 1f, dist + 1f))
                .build());
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper,
                mkd.test(ctx),
                "MatchKillerDistance check should pass"));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void failDistance(GameTestHelper helper) {
        Player player = helper.makeMockPlayer();
        Cow cow = GameTestUtils.simpleEntity(EntityType.COW, helper.getLevel(), TEST_POS);
        LootContext ctx = cow
                .createLootContext(true, DamageSource.playerAttack(player))
                .create(LootContextParamSets.ENTITY);

        MatchKillerDistance mkd = new MatchKillerDistance(new DistancePredicateBuilder()
                .absolute(new MinMaxBounds.Doubles(1000d, 1000d))
                .build());
        helper.succeedIf(() -> GameTestUtils.assertFalse(helper,
                mkd.test(ctx),
                "MatchKillerDistance check should fail"));
    }
}
