package testmod.gametest.tables;

import com.almostreliable.lootjs.BuildConfig;
import com.almostreliable.lootjs.loot.extension.LootTableExtension;
import com.almostreliable.lootjs.loot.table.MutableLootTable;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import testmod.gametest.GameTestTemplates;
import testmod.gametest.GameTestUtils;

@GameTestHolder(value = BuildConfig.MOD_ID)
@PrefixGameTestTemplate(false)
public class LootTableTests {

    private static final Identifier BURIED_TREASURE = Identifier.parse("minecraft:chests/buried_treasure"); //6 pools
    private static final Identifier PILLAGER_OUTPOST = Identifier.parse("minecraft:chests/pillager_outpost"); //6 pools
    private static final Identifier ELDER_GUARDIAN = Identifier.parse("minecraft:entities/elder_guardian"); //5 pools
    private static final Identifier BASTION_BRIDGE = Identifier.parse("minecraft:chests/bastion_bridge"); //5 pools
    private static final Identifier BASTION_OTHER = Identifier.parse("minecraft:chests/bastion_other"); //5 pools
    private static final Identifier BASTION_TREASURE = Identifier.parse("minecraft:chests/bastion_treasure"); //4 pools
    private static final Identifier SPAWN_BONUS_CHEST = Identifier.parse(
            "minecraft:chests/spawn_bonus_chest"); //4 pools
    private static final Identifier BASTION_HOGLIN_STABLE = Identifier.parse(
            "minecraft:chests/bastion_hoglin_stable"); //4 pools
    private static final Identifier WOODLAND_MANSION = Identifier.parse("minecraft:chests/woodland_mansion"); //4 pools
    private static final Identifier SIMPLE_DUNGEON = Identifier.parse("minecraft:chests/simple_dungeon"); //3 pools
    private static final Identifier DARK_OAK_LEAVES = Identifier.parse("minecraft:blocks/dark_oak_leaves"); //3 pools
    private static final Identifier SHIPWRECK_MAP = Identifier.parse("minecraft:chests/shipwreck_map"); //3 pools
    private static final Identifier ZOMBIFIED_PIGLIN = Identifier.parse(
            "minecraft:entities/zombified_piglin"); //3 pools
    private static final Identifier ABANDONED_MINESHAFT = Identifier.parse(
            "minecraft:chests/abandoned_mineshaft"); //3 pools
    private static final Identifier SHIPWRECK_TREASURE = Identifier.parse(
            "minecraft:chests/shipwreck_treasure"); //3 pools
    private static final Identifier GUARDIAN = Identifier.parse("minecraft:entities/guardian"); //3 pools
    private static final Identifier WITHER_SKELETON = Identifier.parse("minecraft:entities/wither_skeleton"); //3 pools
    private static final Identifier RABBIT = Identifier.parse("minecraft:entities/rabbit"); //3 pools
    private static final Identifier DESERT_PYRAMID = Identifier.parse("minecraft:chests/desert_pyramid"); //3 pools
    private static final Identifier STRAY = Identifier.parse("minecraft:entities/stray"); //3 pools
    private static final Identifier POTATOES = Identifier.parse("minecraft:blocks/potatoes"); //3 pools
    private static final Identifier OAK_LEAVES = Identifier.parse("minecraft:blocks/oak_leaves"); //3 pools

    private static MutableLootTable lootTable() {
        return new MutableLootTable(LootContextParamSets.ALL_PARAMS, Identifier.parse("testmod:some_table"));
    }

    private static LootTable getTable(GameTestHelper helper, Identifier location) {
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
