package testmod.gametest.tables;

import com.almostreliable.lootjs.BuildConfig;
import testmod.gametest.GameTestTemplates;
import testmod.gametest.GameTestUtils;
import com.almostreliable.lootjs.loot.extension.LootTableExtension;
import com.almostreliable.lootjs.loot.table.MutableLootTable;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(value = BuildConfig.MOD_ID)
@PrefixGameTestTemplate(false)
public class LootTableTests {

    private static final ResourceLocation BURIED_TREASURE = new ResourceLocation("minecraft:chests/buried_treasure"); //6 pools
    private static final ResourceLocation PILLAGER_OUTPOST = new ResourceLocation("minecraft:chests/pillager_outpost"); //6 pools
    private static final ResourceLocation ELDER_GUARDIAN = new ResourceLocation("minecraft:entities/elder_guardian"); //5 pools
    private static final ResourceLocation BASTION_BRIDGE = new ResourceLocation("minecraft:chests/bastion_bridge"); //5 pools
    private static final ResourceLocation BASTION_OTHER = new ResourceLocation("minecraft:chests/bastion_other"); //5 pools
    private static final ResourceLocation BASTION_TREASURE = new ResourceLocation("minecraft:chests/bastion_treasure"); //4 pools
    private static final ResourceLocation SPAWN_BONUS_CHEST = new ResourceLocation("minecraft:chests/spawn_bonus_chest"); //4 pools
    private static final ResourceLocation BASTION_HOGLIN_STABLE = new ResourceLocation(
            "minecraft:chests/bastion_hoglin_stable"); //4 pools
    private static final ResourceLocation WOODLAND_MANSION = new ResourceLocation("minecraft:chests/woodland_mansion"); //4 pools
    private static final ResourceLocation SIMPLE_DUNGEON = new ResourceLocation("minecraft:chests/simple_dungeon"); //3 pools
    private static final ResourceLocation DARK_OAK_LEAVES = new ResourceLocation("minecraft:blocks/dark_oak_leaves"); //3 pools
    private static final ResourceLocation SHIPWRECK_MAP = new ResourceLocation("minecraft:chests/shipwreck_map"); //3 pools
    private static final ResourceLocation ZOMBIFIED_PIGLIN = new ResourceLocation("minecraft:entities/zombified_piglin"); //3 pools
    private static final ResourceLocation ABANDONED_MINESHAFT = new ResourceLocation(
            "minecraft:chests/abandoned_mineshaft"); //3 pools
    private static final ResourceLocation SHIPWRECK_TREASURE = new ResourceLocation(
            "minecraft:chests/shipwreck_treasure"); //3 pools
    private static final ResourceLocation GUARDIAN = new ResourceLocation("minecraft:entities/guardian"); //3 pools
    private static final ResourceLocation WITHER_SKELETON = new ResourceLocation("minecraft:entities/wither_skeleton"); //3 pools
    private static final ResourceLocation RABBIT = new ResourceLocation("minecraft:entities/rabbit"); //3 pools
    private static final ResourceLocation DESERT_PYRAMID = new ResourceLocation("minecraft:chests/desert_pyramid"); //3 pools
    private static final ResourceLocation STRAY = new ResourceLocation("minecraft:entities/stray"); //3 pools
    private static final ResourceLocation POTATOES = new ResourceLocation("minecraft:blocks/potatoes"); //3 pools
    private static final ResourceLocation OAK_LEAVES = new ResourceLocation("minecraft:blocks/oak_leaves"); //3 pools

    private static MutableLootTable lootTable() {
        return new MutableLootTable(LootContextParamSets.ALL_PARAMS, new ResourceLocation("testmod:some_table"));
    }

    @SuppressWarnings("DataFlowIssue")
    private static LootTable getTable(GameTestHelper helper, ResourceLocation location) {
        return helper
                .getLevel()
                .getServer()
                .getLootData()
                .getElement(LootDataType.TABLE, location);
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
