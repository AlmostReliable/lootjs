package com.almostreliable.lootjs.forge.gametest.conditions;

import com.almostreliable.lootjs.BuildConfig;
import com.almostreliable.lootjs.core.LootJSParamSets;
import com.almostreliable.lootjs.core.LootContextType;
import com.almostreliable.lootjs.forge.gametest.GameTestTemplates;
import com.almostreliable.lootjs.forge.gametest.GameTestUtils;
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
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(value = BuildConfig.MOD_ID)
@PrefixGameTestTemplate(false)
public class ParamSetTest {

    private static final Vec3 TEST_POS = new Vec3(0, 0, 0);

    @GameTest(template = GameTestTemplates.EMPTY)
    public void chest(GameTestHelper helper) {
        LootContext ctx = GameTestUtils.chestContext(helper.getLevel(), TEST_POS, null);

        helper.succeedIf(() -> GameTestUtils.assertEquals(helper,
                ctx.getParam(LootJSParamSets.DATA).getLootContextType(),
                LootContextType.CHEST));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void entity(GameTestHelper helper) {
        Cow cow = GameTestUtils.simpleEntity(EntityType.COW, helper.getLevel(), new BlockPos(TEST_POS));
        LootContext ctx = cow
                .createLootContext(true, DamageSource.FALL)
                .create(LootContextParamSets.ENTITY);

        helper.succeedIf(() -> GameTestUtils.assertEquals(helper,
                ctx.getParam(LootJSParamSets.DATA).getLootContextType(),
                LootContextType.ENTITY));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void block(GameTestHelper helper) {
        LootContext ctx = new LootContext.Builder(helper.getLevel())
                .withRandom(helper.getLevel().getRandom())
                .withParameter(LootContextParams.ORIGIN, TEST_POS)
                .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                .withParameter(LootContextParams.BLOCK_STATE, helper.getLevel().getBlockState(new BlockPos(TEST_POS)))
                .create(LootContextParamSets.CHEST);

        helper.succeedIf(() -> GameTestUtils.assertEquals(helper,
                ctx.getParam(LootJSParamSets.DATA).getLootContextType(),
                LootContextType.CHEST));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void fishing(GameTestHelper helper) {
        Player player = helper.makeMockPlayer();
        FishingHook martin = new FishingHook(player, helper.getLevel(), 0, 0);
        LootContext.Builder builder = new LootContext.Builder(helper.getLevel())
                .withParameter(LootContextParams.ORIGIN, TEST_POS)
                .withParameter(LootContextParams.TOOL, player.getMainHandItem())
                .withParameter(LootContextParams.THIS_ENTITY, martin)
                .withOptionalParameter(LootContextParams.KILLER_ENTITY, player)
                .withRandom(helper.getLevel().getRandom())
                .withLuck(0);

        LootContext ctx = builder.create(LootContextParamSets.FISHING);
        helper.succeedIf(() -> GameTestUtils.assertEquals(helper,
                ctx.getParam(LootJSParamSets.DATA).getLootContextType(),
                LootContextType.FISHING));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void unknown(GameTestHelper helper) {
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), TEST_POS);
        helper.succeedIf(() -> GameTestUtils.assertEquals(helper,
                ctx.getParam(LootJSParamSets.DATA).getLootContextType(),
                LootContextType.UNKNOWN));
    }
}
