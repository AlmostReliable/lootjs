package testmod.gametest.tables;

import com.almostreliable.lootjs.BuildConfig;
import com.almostreliable.lootjs.loot.extension.LootTableExtension;
import com.almostreliable.lootjs.loot.table.MutableLootTable;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import testmod.gametest.GameTestTemplates;
import testmod.gametest.GameTestUtils;

@GameTestHolder(value = BuildConfig.MOD_ID)
@PrefixGameTestTemplate(false)
public class LootTableTests {

    private static final ResourceLocation BURIED_TREASURE = ResourceLocation.parse("minecraft:chests/buried_treasure"); //6 pools
    private static final ResourceLocation PILLAGER_OUTPOST = ResourceLocation.parse("minecraft:chests/pillager_outpost"); //6 pools
    private static final ResourceLocation ELDER_GUARDIAN = ResourceLocation.parse("minecraft:entities/elder_guardian"); //5 pools
    private static final ResourceLocation BASTION_BRIDGE = ResourceLocation.parse("minecraft:chests/bastion_bridge"); //5 pools
    private static final ResourceLocation BASTION_OTHER = ResourceLocation.parse("minecraft:chests/bastion_other"); //5 pools
    private static final ResourceLocation BASTION_TREASURE = ResourceLocation.parse("minecraft:chests/bastion_treasure"); //4 pools
    private static final ResourceLocation SPAWN_BONUS_CHEST = ResourceLocation.parse(
            "minecraft:chests/spawn_bonus_chest"); //4 pools
    private static final ResourceLocation BASTION_HOGLIN_STABLE = ResourceLocation.parse(
            "minecraft:chests/bastion_hoglin_stable"); //4 pools
    private static final ResourceLocation WOODLAND_MANSION = ResourceLocation.parse("minecraft:chests/woodland_mansion"); //4 pools
    private static final ResourceLocation SIMPLE_DUNGEON = ResourceLocation.parse("minecraft:chests/simple_dungeon"); //3 pools
    private static final ResourceLocation DARK_OAK_LEAVES = ResourceLocation.parse("minecraft:blocks/dark_oak_leaves"); //3 pools
    private static final ResourceLocation SHIPWRECK_MAP = ResourceLocation.parse("minecraft:chests/shipwreck_map"); //3 pools
    private static final ResourceLocation ZOMBIFIED_PIGLIN = ResourceLocation.parse(
            "minecraft:entities/zombified_piglin"); //3 pools
    private static final ResourceLocation ABANDONED_MINESHAFT = ResourceLocation.parse(
            "minecraft:chests/abandoned_mineshaft"); //3 pools
    private static final ResourceLocation SHIPWRECK_TREASURE = ResourceLocation.parse(
            "minecraft:chests/shipwreck_treasure"); //3 pools
    private static final ResourceLocation GUARDIAN = ResourceLocation.parse("minecraft:entities/guardian"); //3 pools
    private static final ResourceLocation WITHER_SKELETON = ResourceLocation.parse("minecraft:entities/wither_skeleton"); //3 pools
    private static final ResourceLocation RABBIT = ResourceLocation.parse("minecraft:entities/rabbit"); //3 pools
    private static final ResourceLocation DESERT_PYRAMID = ResourceLocation.parse("minecraft:chests/desert_pyramid"); //3 pools
    private static final ResourceLocation STRAY = ResourceLocation.parse("minecraft:entities/stray"); //3 pools
    private static final ResourceLocation POTATOES = ResourceLocation.parse("minecraft:blocks/potatoes"); //3 pools
    private static final ResourceLocation OAK_LEAVES = ResourceLocation.parse("minecraft:blocks/oak_leaves"); //3 pools

    private static MutableLootTable lootTable() {
        return new MutableLootTable(LootContextParamSets.ALL_PARAMS, ResourceLocation.parse("testmod:some_table"));
    }

    private static LootTable getTable(GameTestHelper helper, ResourceLocation location) {
        return helper
                .getLevel()
                .getServer()
                .reloadableRegistries()
                .getLootTable(ResourceKey.create(Registries.LOOT_TABLE, location));
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void empty(GameTestHelper helper) {
        helper.succeedIf(() -> {
            var table = getTable(helper, BURIED_TREASURE);
            int oldSize = LootTableExtension.cast(table).lootjs$getPools().size();
            new MutableLootTable(table).createPool(mutableLootPool -> {}).writeToVanillaTable();
            int newSize = LootTableExtension.cast(table).lootjs$getPools().size();
            GameTestUtils.assertEquals(helper, oldSize + 1, newSize);
        });
    }

}
