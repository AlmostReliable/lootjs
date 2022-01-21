package com.github.llytho.lootjs.core;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;

import java.util.HashSet;
import java.util.List;

public class LootModificationByEntity extends AbstractLootModification {

    public final HashSet<EntityType<?>> entities;

    public LootModificationByEntity(String name, HashSet<EntityType<?>> entities, List<ILootAction> actions) {
        super(name, actions);
        this.entities = entities;
    }

    @Override
    public boolean shouldExecute(LootContext context) {
        Entity entity = context.getParamOrNull(LootParameters.THIS_ENTITY);
        return entity != null && entities.contains(entity.getType());
    }
}
