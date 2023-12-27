package com.almostreliable.lootjs.gametest.conditions;

import com.almostreliable.lootjs.BuildConfig;
import com.almostreliable.lootjs.gametest.GameTestTemplates;
import com.almostreliable.lootjs.gametest.GameTestUtils;
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
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(value = BuildConfig.MOD_ID)
@PrefixGameTestTemplate(false)
public class MatchKillerDistanceTest {
    private static final BlockPos TEST_POS = new BlockPos(0, 0, 0);

    @GameTest(template = GameTestTemplates.EMPTY)
    public void matchDistance(GameTestHelper helper) {
        Player player = helper.makeMockPlayer();
        Cow cow = GameTestUtils.simpleEntity(EntityType.COW, helper.getLevel(), TEST_POS);

        DamageSource ds = cow.damageSources().playerAttack(player);
        var params = new LootParams.Builder(helper.getLevel())
                .withParameter(LootContextParams.THIS_ENTITY, cow)
                .withParameter(LootContextParams.ORIGIN, cow.position())
                .withParameter(LootContextParams.DAMAGE_SOURCE, ds)
                .withOptionalParameter(LootContextParams.KILLER_ENTITY, ds.getEntity())
                .withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, ds.getDirectEntity())
                .withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player)
                .withLuck(player.getLuck())
                .create(LootContextParamSets.ENTITY);

        LootContext ctx = new LootContext.Builder(params).create(null);

        double dist = player.distanceTo(cow);
        MatchKillerDistance mkd = new MatchKillerDistance(new DistancePredicateBuilder()
                .absolute(MinMaxBounds.Doubles.between(dist - 1f, dist + 1f))
                .build());
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper,
                mkd.test(ctx),
                "MatchKillerDistance check should pass"));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void failDistance(GameTestHelper helper) {
        Player player = helper.makeMockPlayer();
        Cow cow = GameTestUtils.simpleEntity(EntityType.COW, helper.getLevel(), TEST_POS);

        DamageSource ds = cow.damageSources().playerAttack(player);
        var params = new LootParams.Builder(helper.getLevel())
                .withParameter(LootContextParams.THIS_ENTITY, cow)
                .withParameter(LootContextParams.ORIGIN, cow.position())
                .withParameter(LootContextParams.DAMAGE_SOURCE, ds)
                .withOptionalParameter(LootContextParams.KILLER_ENTITY, ds.getEntity())
                .withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, ds.getDirectEntity())
                .withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player)
                .withLuck(player.getLuck())
                .create(LootContextParamSets.ENTITY);

        LootContext ctx = new LootContext.Builder(params).create(null);

        MatchKillerDistance mkd = new MatchKillerDistance(new DistancePredicateBuilder()
                .absolute(MinMaxBounds.Doubles.between(1000d, 1000d))
                .build());
        helper.succeedIf(() -> GameTestUtils.assertFalse(helper,
                mkd.test(ctx),
                "MatchKillerDistance check should fail"));
    }
}
