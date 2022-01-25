package com.github.llytho.lootjs.loot.condition;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class AnyStructure implements IExtendedLootCondition {

    private final StructureFeature<?>[] structures;
    private final boolean exact;

    public AnyStructure(StructureFeature<?>[] structures, boolean exact) {
        this.structures = structures;
        this.exact = exact;
    }

    @Override
    public boolean test(LootContext context) {
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);

        if (origin != null) {
            BlockPos blockPos = new BlockPos(origin.x, origin.y, origin.z);
            StructureFeatureManager sfm = context.getLevel().structureFeatureManager();
            for (StructureFeature<?> structure : structures) {
                // TODO testing this
                StructureStart<?> structureAt = exact ? sfm.getStructureWithPieceAt(blockPos, structure)
                                                      : sfm.getStructureAt(blockPos, structure);
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

    public StructureFeature<?>[] getStructures() {
        return structures;
    }
}
