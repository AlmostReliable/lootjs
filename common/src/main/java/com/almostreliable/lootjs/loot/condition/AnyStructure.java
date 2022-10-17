package com.almostreliable.lootjs.loot.condition;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.List;

public class AnyStructure implements IExtendedLootCondition {

    private final List<ResourceKey<Structure>> structures;
    private final boolean exact;

    public AnyStructure(List<ResourceKey<Structure>> structures, boolean exact) {
        this.structures = structures;
        this.exact = exact;
    }

    @Override
    public boolean test(LootContext context) {
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);

        if (origin != null) {
            BlockPos blockPos = new BlockPos(origin.x, origin.y, origin.z);
            StructureManager structureManager = context.getLevel().structureManager();
            Registry<Structure> registry = context
                    .getLevel()
                    .registryAccess()
                    .registryOrThrow(Registry.STRUCTURE_REGISTRY);
            for (var resourceKey : structures) {
                Structure feature = registry.get(resourceKey);
                if (feature != null) {
                    var structureAt = exact
                                      ? structureManager.getStructureWithPieceAt(blockPos, feature)
                                      : structureManager.getStructureAt(blockPos, feature);
                    if (structureAt.isValid()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isExact() {
        return exact;
    }

    public List<ResourceKey<Structure>> getStructuresOld() {
        return Collections.unmodifiableList(structures);
    }
}
