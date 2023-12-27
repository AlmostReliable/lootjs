package com.almostreliable.lootjs.mixin;

import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LootItemCondition.class)
@RemapPrefixForJS("lootjs$")
public interface LootItemConditionMixin {


    @Unique
    default LootItemCondition lootjs$not() {
        return new InvertedLootItemCondition((LootItemCondition) this);
    }

    @Unique
    default LootItemCondition lootjs$or(LootItemCondition other) {
        return new AnyOfCondition(new LootItemCondition[]{ (LootItemCondition) this, other });
    }

    @Unique
    default LootItemCondition lootjs$and(LootItemCondition other) {
        return new AllOfCondition(new LootItemCondition[]{ (LootItemCondition) this, other });
    }
}
