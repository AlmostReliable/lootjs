package com.almostreliable.lootjs.mixin;

import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(LootDataManager.class)
public interface LootDataManagerAccessor {

    @Accessor()
    Map<LootDataId<?>, ?> getElements();

    @Accessor()
    void setElements(Map<LootDataId<?>, ?> elements);
}
