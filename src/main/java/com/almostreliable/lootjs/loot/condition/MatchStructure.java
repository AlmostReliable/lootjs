package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.LootJSConditions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.phys.Vec3;

public record MatchStructure(HolderSet<Structure> structures, boolean exact) implements LootItemCondition {

    @Override
    public boolean test(LootContext context) {
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        if (origin == null) {
            return false;
        }

        var blockPos = new BlockPos((int) origin.x, (int) origin.y, (int) origin.z);
        var structureManager = context.getLevel().structureManager();

        if (exact) {
            var start = structureManager.getStructureWithPieceAt(blockPos, structures);
            return start.isValid();
        }

        for (var structure : structures) {
            var start = structureManager.getStructureAt(blockPos, structure.value());
            if (start.isValid()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public LootItemConditionType getType() {
        return LootJSConditions.ANY_STRUCTURE.value();
    }
}
