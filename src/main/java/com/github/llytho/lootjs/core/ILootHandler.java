package com.github.llytho.lootjs.core;

import net.minecraft.world.level.storage.loot.LootContext;

import java.util.function.Predicate;

public interface ILootHandler extends Predicate<LootContext> {}
