package com.almostreliable.lootjs.forge.gametest.conditions;

import com.almostreliable.lootjs.BuildConfig;
import com.almostreliable.lootjs.core.LootType;
import com.almostreliable.lootjs.forge.gametest.GameTestTemplates;
import com.almostreliable.lootjs.forge.gametest.GameTestUtils;
import com.almostreliable.lootjs.loot.extension.LootContextExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(value = BuildConfig.MOD_ID)
@PrefixGameTestTemplate(false)
public class ParamSetTest {

    private static final Vec3 TEST_POS = new Vec3(0, 0, 0);
    private static final BlockPos TEST_BLOCK_POS = new BlockPos(0, 0, 0);

    @GameTest(template = GameTestTemplates.EMPTY)
    public void chest(GameTestHelper helper) {
        LootContext ctx = GameTestUtils.chestContext(helper.getLevel(), TEST_POS, null);

        helper.succeedIf(() -> GameTestUtils.assertEquals(helper,
                LootContextExtension.cast(ctx).lootjs$getType(),
                LootType.CHEST));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void entity(GameTestHelper helper) {
        Cow cow = GameTestUtils.simpleEntity(EntityType.COW, helper.getLevel(), TEST_BLOCK_POS);

        DamageSource ds = cow.damageSources().fall();
        var params = new LootParams.Builder(helper.getLevel())
                .withParameter(LootContextParams.THIS_ENTITY, cow)
                .withParameter(LootContextParams.ORIGIN, cow.position())
                .withParameter(LootContextParams.DAMAGE_SOURCE, ds)
                .withOptionalParameter(LootContextParams.KILLER_ENTITY, ds.getEntity())
                .withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, ds.getDirectEntity())
                .create(LootContextParamSets.ENTITY);

        LootContext ctx = new LootContext.Builder(params).create(null);

        helper.succeedIf(() -> GameTestUtils.assertEquals(helper,
                LootContextExtension.cast(ctx).lootjs$getType(),
                LootType.ENTITY));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void block(GameTestHelper helper) {
        var params = new LootParams.Builder(helper.getLevel())
                .withParameter(LootContextParams.ORIGIN, TEST_POS)
                .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                .withParameter(LootContextParams.BLOCK_STATE, helper.getLevel().getBlockState(TEST_BLOCK_POS))
                .create(LootContextParamSets.CHEST);

        LootContext ctx = new LootContext.Builder(params).create(null);

        helper.succeedIf(() -> GameTestUtils.assertEquals(helper,
                LootContextExtension.cast(ctx).lootjs$getType(),
                LootType.CHEST));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void fishing(GameTestHelper helper) {
        Player player = helper.makeMockPlayer();
        FishingHook martin = new FishingHook(player, helper.getLevel(), 0, 0);
        var params = new LootParams.Builder(helper.getLevel())
                .withParameter(LootContextParams.ORIGIN, TEST_POS)
                .withParameter(LootContextParams.TOOL, player.getMainHandItem())
                .withParameter(LootContextParams.THIS_ENTITY, martin)
                .withOptionalParameter(LootContextParams.KILLER_ENTITY, player)
                .withLuck(0)
                .create(LootContextParamSets.FISHING);

        LootContext ctx = new LootContext.Builder(params).create(null);
        helper.succeedIf(() -> GameTestUtils.assertEquals(helper,
                LootContextExtension.cast(ctx).lootjs$getType(),
                LootType.FISHING));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void unknown(GameTestHelper helper) {
        var set = new LootContextParamSet.Builder().required(LootContextParams.ORIGIN).build();
        var params = new LootParams.Builder(helper.getLevel())
                .withParameter(LootContextParams.ORIGIN, TEST_POS)
                .create(set);

        LootContext ctx = new LootContext.Builder(params).create(null);
        helper.succeedIf(() -> GameTestUtils.assertEquals(helper,
                LootContextExtension.cast(ctx).lootjs$getType(),
                LootType.UNKNOWN));
    }
}
