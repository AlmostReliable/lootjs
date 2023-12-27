package com.almostreliable.lootjs.gametest.conditions;

import com.almostreliable.lootjs.BuildConfig;
import com.almostreliable.lootjs.gametest.GameTestTemplates;
import com.almostreliable.lootjs.gametest.GameTestUtils;
import com.almostreliable.lootjs.loot.condition.AnyBiomeCheck;
import com.almostreliable.lootjs.loot.condition.BiomeCheck;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootContext;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

@GameTestHolder(value = BuildConfig.MOD_ID)
@PrefixGameTestTemplate(false)
public class BiomeCheckTest {

    private static final BlockPos TEST_POS = new BlockPos(1, 0, 1);

    @GameTest(template = GameTestTemplates.EMPTY)
    public void AnyBiomeCheck_match(GameTestHelper helper) {
        Player player = helper.makeMockPlayer();
        Holder<Biome> biomeHolder = helper.getLevel().getBiome(TEST_POS);
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), player.position());

        ResourceLocation biome = helper
                .getLevel()
                .registryAccess()
                .registryOrThrow(Registries.BIOME)
                .getKey(biomeHolder.value());
        ResourceKey<Biome> bKey = biome(biome);
        AnyBiomeCheck check = new AnyBiomeCheck(Collections.singletonList(bKey), new ArrayList<>());
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper,
                check.test(ctx),
                "Biome " + bKey.location() + " check should pass"));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void BiomeCheck_match(GameTestHelper helper) {
        Player player = helper.makeMockPlayer();
        Holder<Biome> biomeHolder = helper.getLevel().getBiome(TEST_POS);
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), player.position());

        ResourceLocation biome = helper
                .getLevel()
                .registryAccess()
                .registryOrThrow(Registries.BIOME)
                .getKey(biomeHolder.value());
        ResourceKey<Biome> bKey = biome(biome);
        BiomeCheck check = new BiomeCheck(Collections.singletonList(bKey), new ArrayList<>());
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper,
                check.test(ctx),
                "Biome " + bKey.location() + " check should pass"));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void AnyBiomeCheck_fail(GameTestHelper helper) {
        Player player = helper.makeMockPlayer();
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), player.position());

        ResourceKey<Biome> bKey = biome(new ResourceLocation("minecraft:deep_ocean"));
        AnyBiomeCheck check = new AnyBiomeCheck(Collections.singletonList(bKey), new ArrayList<>());
        helper.succeedIf(() -> GameTestUtils.assertFalse(helper,
                check.test(ctx),
                "Biome " + bKey.location() + " check should not pass"));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void BiomeCheck_fail(GameTestHelper helper) {
        Player player = helper.makeMockPlayer();
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), player.position());

        ResourceKey<Biome> bKey = biome(new ResourceLocation("minecraft:deep_ocean"));
        BiomeCheck check = new BiomeCheck(Collections.singletonList(bKey), new ArrayList<>());
        helper.succeedIf(() -> GameTestUtils.assertFalse(helper,
                check.test(ctx),
                "Biome " + bKey.location() + " check should not pass"));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void AnyBiomeCheck_matchTags(GameTestHelper helper) {
        Player player = helper.makeMockPlayer();
        Holder<Biome> biomeHolder = helper.getLevel().getBiome(TEST_POS);
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), player.position());

        var types = biomeHolder.tags().toList();
        AnyBiomeCheck check = new AnyBiomeCheck(new ArrayList<>(), new ArrayList<>(types));
        var biomeReg = helper.getLevel().registryAccess().registryOrThrow(Registries.BIOME);
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper,
                check.test(ctx),
                "Biome " + biomeReg.getKey(biomeHolder.value()) + " tag check should pass"));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void BiomeCheck_matchAllTags(GameTestHelper helper) {
        Player player = helper.makeMockPlayer();
        Holder<Biome> biomeHolder = helper.getLevel().getBiome(TEST_POS);
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), player.position());

        var types = biomeHolder.tags().toList();
        BiomeCheck check = new BiomeCheck(new ArrayList<>(), new ArrayList<>(types));
        var biomeReg = helper.getLevel().registryAccess().registryOrThrow(Registries.BIOME);
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper,
                check.test(ctx),
                "Biome " + biomeReg.getKey(biomeHolder.value()) + " tag check should pass"));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void AnyBiomeCheck_failAllTags(GameTestHelper helper) {
        Player player = helper.makeMockPlayer();
        Holder<Biome> biomeHolder = helper.getLevel().getBiome(TEST_POS);
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), player.position());

        AnyBiomeCheck check = new AnyBiomeCheck(new ArrayList<>(), Collections.singletonList(BiomeTags.IS_NETHER));
        var biomeReg = helper.getLevel().registryAccess().registryOrThrow(Registries.BIOME);
        helper.succeedIf(() -> GameTestUtils.assertFalse(helper,
                check.test(ctx),
                "Biome " + biomeReg.getKey(biomeHolder.value()) + " tag check should not pass"));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void BiomeCheck_failAllTags(GameTestHelper helper) {
        Player player = helper.makeMockPlayer();
        Holder<Biome> biomeHolder = helper.getLevel().getBiome(TEST_POS);
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), player.position());

        BiomeCheck check = new BiomeCheck(new ArrayList<>(), Collections.singletonList(BiomeTags.IS_NETHER));
        var biomeReg = helper.getLevel().registryAccess().registryOrThrow(Registries.BIOME);
        helper.succeedIf(() -> GameTestUtils.assertFalse(helper,
                check.test(ctx),
                "Biome " + biomeReg.getKey(biomeHolder.value()) + " tag check should not pass"));
    }

    public ResourceKey<Biome> biome(ResourceLocation biome) {
        return ResourceKey.create(Registries.BIOME, Objects.requireNonNull(biome));
    }
}
