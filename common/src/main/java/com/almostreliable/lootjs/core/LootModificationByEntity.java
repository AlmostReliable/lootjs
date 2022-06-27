package com.almostreliable.lootjs.core;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.HashSet;
import java.util.List;

public class LootModificationByEntity extends AbstractLootModification {

    public final HashSet<EntityType<?>> entities;

    public LootModificationByEntity(String name, HashSet<EntityType<?>> entities, List<ILootHandler> handlers) {
        super(name, handlers);
        this.entities = entities;
    }

    @Override
    public boolean shouldExecute(LootContext context) {
        ILootContextData data = context.getParamOrNull(Constants.DATA);
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        return entity != null && data != null && entities.contains(entity.getType()) && data.getLootContextType() == LootContextType.ENTITY;
    }
}
