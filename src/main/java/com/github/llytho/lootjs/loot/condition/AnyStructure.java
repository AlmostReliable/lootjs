package com.github.llytho.lootjs.loot.condition;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.List;

public class AnyStructure implements IExtendedLootCondition {

    private final List<ResourceKey<ConfiguredStructureFeature<?, ?>>> structures;
    private final boolean exact;

    public AnyStructure(List<ResourceKey<ConfiguredStructureFeature<?, ?>>> structures, boolean exact) {
        this.structures = structures;
        this.exact = exact;
    }

    @Override
    public boolean test(LootContext context) {
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);

        if (origin != null) {
            BlockPos blockPos = new BlockPos(origin.x, origin.y, origin.z);
            StructureFeatureManager sfm = context.getLevel().structureFeatureManager();
            Registry<ConfiguredStructureFeature<?, ?>> registry = context
                    .getLevel()
                    .registryAccess()
                    .registryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
            for (var resourceKey : structures) {
                ConfiguredStructureFeature<?, ?> feature = registry.get(resourceKey);
                if (feature != null) {
                    var structureAt = exact
                                      ? sfm.getStructureWithPieceAt(blockPos, feature)
                                      : sfm.getStructureAt(blockPos, feature);
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

    public List<ResourceKey<ConfiguredStructureFeature<?, ?>>> getStructuresOld() {
        return Collections.unmodifiableList(structures);
    }
}
