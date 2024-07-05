package testmod.gametest.conditions;

import com.almostreliable.lootjs.BuildConfig;
import com.almostreliable.lootjs.loot.condition.MatchBiome;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import testmod.gametest.GameTestTemplates;
import testmod.gametest.GameTestUtils;

@GameTestHolder(value = BuildConfig.MOD_ID)
@PrefixGameTestTemplate(false)
public class BiomeCheckTest {

    private static final BlockPos TEST_POS = new BlockPos(1, 0, 1);

    @GameTest(template = GameTestTemplates.EMPTY)
    public void BiomeCheck_match(GameTestHelper helper) {
        Holder<Biome> biomeHolder = helper.getLevel().getBiome(TEST_POS);
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), Vec3.atLowerCornerOf(TEST_POS));
        MatchBiome check = new MatchBiome(HolderSet.direct(biomeHolder));
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper,
                check.test(ctx),
                "Biome " + biomeHolder + " check should pass"));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void BiomeCheck_fail(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), player.position());

        var biomeReference = helper
                .getLevel()
                .registryAccess()
                .registryOrThrow(Registries.BIOME)
                .getHolder(ResourceLocation.parse("minecraft:deep_ocean"))
                .orElseThrow();
        MatchBiome check = new MatchBiome(HolderSet.direct(biomeReference));
        helper.succeedIf(() -> GameTestUtils.assertFalse(helper,
                check.test(ctx),
                "Biome " + biomeReference.key() + " check should not pass"));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void BiomeCheck_matchTags(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), player.position());
        Registry<Biome> biomes = helper.getLevel().registryAccess().registryOrThrow(Registries.BIOME);
        MatchBiome check = new MatchBiome(biomes.getOrCreateTag(BiomeTags.IS_OVERWORLD));
        helper.succeedIf(() -> GameTestUtils.assertTrue(helper,
                check.test(ctx),
                "Biome " + BiomeTags.IS_OVERWORLD + " tag check should pass"));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void BiomeCheck_failAllTags(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        LootContext ctx = GameTestUtils.unknownContext(helper.getLevel(), player.position());
        Registry<Biome> biomes = helper.getLevel().registryAccess().registryOrThrow(Registries.BIOME);
        MatchBiome check = new MatchBiome(biomes.getOrCreateTag(BiomeTags.IS_NETHER));
        helper.succeedIf(() -> GameTestUtils.assertFalse(helper,
                check.test(ctx),
                "Biome " + BiomeTags.IS_NETHER + " tag check should not pass"));
    }
}
