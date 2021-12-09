package com.github.llytho.lootjs.condition;

import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;

import java.util.function.Predicate;

public class AnyStructure implements Predicate<LootContext> {

    private final Structure<?>[] structures;

    public AnyStructure(Structure<?>[] pStructures) {
        this.structures = pStructures;
    }

    @Override
    public boolean test(LootContext pContext) {
        Vector3d origin = pContext.getParamOrNull(LootParameters.ORIGIN);

        if (origin != null) {
            BlockPos blockPos = new BlockPos(origin.x, origin.y, origin.z);

            for (Structure<?> structure : structures) {
                StructureStart<?> structureAt = pContext
                        .getLevel()
                        .structureFeatureManager()
                        .getStructureAt(blockPos, true, structure);

                if (structureAt.isValid()) {
                    return true;
                }

            }
        }

        return false;
    }
}
