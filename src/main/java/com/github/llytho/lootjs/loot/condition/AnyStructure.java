package com.github.llytho.lootjs.loot.condition;

import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;

public class AnyStructure implements IExtendedLootCondition {

    private final Structure<?>[] structures;
    private final boolean exact;

    public AnyStructure(Structure<?>[] structures, boolean exact) {
        this.structures = structures;
        this.exact = exact;
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
                        .getStructureAt(blockPos, exact, structure);

                if (structureAt.isValid()) {
                    return true;
                }

            }
        }

        return false;
    }

    public boolean isExact() {
        return exact;
    }

    public Structure<?>[] getStructures() {
        return structures;
    }
}
