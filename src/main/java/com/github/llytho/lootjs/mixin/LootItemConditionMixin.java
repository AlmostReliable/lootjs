package com.github.llytho.lootjs.mixin;

import com.github.llytho.lootjs.core.ILootHandler;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LootItemCondition.class)
public interface LootItemConditionMixin extends ILootHandler {}
