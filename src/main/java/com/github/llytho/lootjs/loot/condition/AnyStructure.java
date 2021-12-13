package com.github.llytho.lootjs.loot.condition;

import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;

public class AnyStructure implements IExtendedLootCondition {

    private final Structure<?>[] structures;

    public AnyStructure(Structure<?>[] structures) {
        this.structures = structures;
    }

    @Override
    public boolean test(LootContext context) {
        Vector3d origin = context.getParamOrNull(LootParameters.ORIGIN);

        if (origin != null) {
            BlockPos blockPos = new BlockPos(origin.x, origin.y, origin.z);

            for (Structure<?> structure : structures) {
                StructureStart<?> structureAt = context
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
