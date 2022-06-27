package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.util.LootContextUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;


public class MainHandTableBonus implements IExtendedLootCondition {
    private final Enchantment enchantment;
    private final float[] values;

    public MainHandTableBonus(Enchantment enchantment, float[] values) {
        this.enchantment = enchantment;
        this.values = values;
    }

    @Override
    public boolean test(LootContext context) {
        ServerPlayer player = LootContextUtils.getPlayerOrNull(context);
        if (player == null) {
            return false;
        }

        int enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(this.enchantment, player.getMainHandItem());
        int minIndex = Math.min(enchantmentLevel, this.values.length - 1);
        return context.getRandom().nextFloat() < this.values[minIndex];
    }
}
