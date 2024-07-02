package testmod.gametest;

import com.almostreliable.lootjs.BuildConfig;
import com.almostreliable.lootjs.core.filters.IdFilter;
import com.almostreliable.lootjs.core.filters.ItemFilter;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.util.List;
import java.util.regex.Pattern;

@GameTestHolder(value = BuildConfig.MOD_ID)
@PrefixGameTestTemplate(false)
public class ItemFilterTests {

    @GameTest(template = GameTestTemplates.EMPTY)
    public void simpleTest(GameTestHelper helper) {
        helper.succeedIf(() -> {
            ExampleLoot loot = new ExampleLoot(helper);
            var rlFilter = new IdFilter.ByLocation(Enchantments.PROJECTILE_PROTECTION.location());
            ItemFilter filter = ItemFilter.hasEnchantment(rlFilter);
            GameTestUtils.assertTrue(helper, filter.test(loot.helmet), "Helmet should pass filter");
            GameTestUtils.assertTrue(helper, filter.test(loot.chestPlate), "Chestplate should pass filter");
            GameTestUtils.assertFalse(helper, filter.test(loot.sword), "Sword should not pass filter");
            GameTestUtils.assertFalse(helper, filter.test(loot.book), "Book should not pass filter");
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void enchantmentFilterWithOr(GameTestHelper helper) {
        helper.succeedIf(() -> {
            ExampleLoot loot = new ExampleLoot(helper);

            var rlFilter = new IdFilter.ByLocation(Enchantments.PROJECTILE_PROTECTION.location());
            ItemFilter filter = ItemFilter.hasEnchantment(rlFilter).or(ItemFilter.tag("#minecraft:swords"));
            GameTestUtils.assertTrue(helper, filter.test(loot.helmet), "Helmet should pass filter");
            GameTestUtils.assertTrue(helper, filter.test(loot.chestPlate), "Chestplate should pass filter");
            GameTestUtils.assertTrue(helper, filter.test(loot.sword), "Sword should pass filter");
            GameTestUtils.assertFalse(helper, filter.test(loot.book), "Book should not pass filter");
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void enchantmentFilterWithAnd(GameTestHelper helper) {
        helper.succeedIf(() -> {
            ExampleLoot loot = new ExampleLoot(helper);
            var rlFilter = new IdFilter.ByLocation(Enchantments.SHARPNESS.location());
            ItemFilter filter = ItemFilter.hasEnchantment(rlFilter).and(ItemFilter.tag("#minecraft:swords"));
            GameTestUtils.assertFalse(helper, filter.test(loot.helmet), "Helmet should not pass filter");
            GameTestUtils.assertFalse(helper, filter.test(loot.chestPlate), "Chestplate not should pass filter");
            GameTestUtils.assertTrue(helper, filter.test(loot.sword), "Sword should pass filter");
            GameTestUtils.assertFalse(helper, filter.test(loot.book), "Book should not pass filter");
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void enchantmentFilterWithAndFails(GameTestHelper helper) {
        helper.succeedIf(() -> {
            ExampleLoot loot = new ExampleLoot(helper);
            var rlFilter = new IdFilter.ByLocation(Enchantments.LOOTING.location());
            ItemFilter filter = ItemFilter.hasEnchantment(rlFilter).and(ItemFilter.tag("#minecraft:swords"));
            GameTestUtils.assertFalse(helper, filter.test(loot.helmet), "Helmet should not pass filter");
            GameTestUtils.assertFalse(helper, filter.test(loot.chestPlate), "Chestplate not should pass filter");
            GameTestUtils.assertFalse(helper, filter.test(loot.sword), "Sword should not pass filter");
            GameTestUtils.assertFalse(helper, filter.test(loot.book), "Book should not pass filter");
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void enchantmentRegex(GameTestHelper helper) {
        helper.succeedIf(() -> {
            ExampleLoot loot = new ExampleLoot(helper);
            ItemFilter filter = ItemFilter.hasEnchantment(new IdFilter.ByPattern(Pattern.compile(
                    ".*sharpness.*")));
            GameTestUtils.assertFalse(helper, filter.test(loot.helmet), "Helmet should not pass filter");
            GameTestUtils.assertFalse(helper, filter.test(loot.chestPlate), "Chestplate should not pass filter");
            GameTestUtils.assertTrue(helper, filter.test(loot.sword), "Sword should pass filter");
            GameTestUtils.assertFalse(helper, filter.test(loot.book), "Book should not pass filter");
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void enchantmentRegexOrLocation(GameTestHelper helper) {
        helper.succeedIf(() -> {
            ExampleLoot loot = new ExampleLoot(helper);
            IdFilter rlf = new IdFilter.Or(List.of(new IdFilter.ByPattern(
                            Pattern.compile(".*sharpness.*")),
                    new IdFilter.ByLocation(Enchantments.INFINITY.location())));
            ItemFilter filter = ItemFilter.hasEnchantment(rlf);
            GameTestUtils.assertFalse(helper, filter.test(loot.helmet), "Helmet should not pass filter");
            GameTestUtils.assertFalse(helper, filter.test(loot.chestPlate), "Chestplate should not pass filter");
            GameTestUtils.assertTrue(helper, filter.test(loot.sword), "Sword should pass filter");
            GameTestUtils.assertFalse(helper,
                    filter.test(loot.book),
                    "Book should not pass filter, because we need to use stored enchantments");

            filter = ItemFilter.hasStoredEnchantment(rlf);
            GameTestUtils.assertTrue(helper, filter.test(loot.book), "Book should pass filter");
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void enchantmentMinInclusive(GameTestHelper helper) {
        helper.succeedIf(() -> {
            Holder.Reference<Enchantment> pp = helper
                    .getLevel()
                    .registryAccess()
                    .registryOrThrow(Registries.ENCHANTMENT)
                    .getHolderOrThrow(Enchantments.PROJECTILE_PROTECTION);
            EnchantmentInstance e = new EnchantmentInstance(pp, 2);
            ItemFilter filter = ItemFilter.hasStoredEnchantment(rl -> rl.equals(pp.key().location()),
                    MinMaxBounds.Ints.between(2, 5));
            GameTestUtils.assertTrue(helper,
                    filter.test(EnchantedBookItem.createForEnchantment(e)),
                    "Enchantment in range");
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void enchantmentMaxInclusive(GameTestHelper helper) {
        helper.succeedIf(() -> {
            Holder.Reference<Enchantment> pp = helper
                    .getLevel()
                    .registryAccess()
                    .registryOrThrow(Registries.ENCHANTMENT)
                    .getHolderOrThrow(Enchantments.PROJECTILE_PROTECTION);
            EnchantmentInstance e = new EnchantmentInstance(pp, 5);
            ItemFilter filter = ItemFilter.hasStoredEnchantment(rl -> rl.equals(pp.key().location()),
                    MinMaxBounds.Ints.between(2, 5));
            GameTestUtils.assertTrue(helper,
                    filter.test(EnchantedBookItem.createForEnchantment(e)),
                    "Enchantment in range");
        });
    }

    private static class ExampleLoot {
        private final ItemStack helmet;
        private final ItemStack sword;
        private final ItemStack book;
        private final ItemStack chestPlate;

        public ExampleLoot(GameTestHelper helper) {
            Registry<Enchantment> enchantments = helper
                    .getLevel()
                    .registryAccess()
                    .registryOrThrow(Registries.ENCHANTMENT);
            this.helmet = new ItemStack(Items.IRON_HELMET);
            this.helmet.enchant(enchantments.getHolderOrThrow(Enchantments.PROJECTILE_PROTECTION), 4);
            this.chestPlate = new ItemStack(Items.IRON_HELMET);
            this.chestPlate.enchant(enchantments.getHolderOrThrow(Enchantments.PROJECTILE_PROTECTION), 4);
            this.sword = new ItemStack(Items.IRON_SWORD);
            this.sword.enchant(enchantments.getHolderOrThrow(Enchantments.SHARPNESS), 4);
            this.book = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantments.getHolderOrThrow(
                    Enchantments.INFINITY), 3));
        }
    }
}
