package com.github.llytho.lootjs.loot.condition;

import com.github.llytho.lootjs.loot.condition.IExtendedLootCondition;
import com.github.llytho.lootjs.util.LootContextUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.LootContext;


public class MainHandTableBonus implements IExtendedLootCondition {
    private final Enchantment enchantment;
    private final float[] values;

    public MainHandTableBonus(Enchantment enchantment, float[] values) {
        this.enchantment = enchantment;
        this.values = values;
    }

    @Override
    public boolean test(LootContext context) {
        ServerPlayerEntity player = LootContextUtils.getPlayerOrNull(context);
        if (player == null) {
            return false;
        }

        int enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(this.enchantment, player.getMainHandItem());
        int minIndex = Math.min(enchantmentLevel, this.values.length - 1);
        return context.getRandom().nextFloat() < this.values[minIndex];
    }
}
