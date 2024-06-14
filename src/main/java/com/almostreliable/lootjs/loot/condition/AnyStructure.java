package com.almostreliable.lootjs.loot.condition;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AnyStructure implements IExtendedLootCondition {

    private final List<StructureLocator> structureLocators;
    private final boolean exact;

    public AnyStructure(List<StructureLocator> structureLocators, boolean exact) {
        this.structureLocators = structureLocators;
        this.exact = exact;
    }

    @Override
    public boolean test(LootContext context) {
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);

        if (origin != null) {
            BlockPos blockPos = new BlockPos((int) origin.x, (int) origin.y, (int) origin.z);
            StructureManager structureManager = context.getLevel().structureManager();
            Registry<Structure> registry = context
                    .getLevel()
                    .registryAccess()
                    .registryOrThrow(Registries.STRUCTURE);


            for (StructureLocator l : structureLocators) {
                Structure feature = l.getStructure(registry, context.getLevel(), blockPos);
                if (feature == null) {
                    continue;
                }
                var structureAt = exact
                                  ? structureManager.getStructureWithPieceAt(blockPos, feature)
                                  : structureManager.getStructureAt(blockPos, feature);
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

    public interface StructureLocator {
        @Nullable
        Structure getStructure(Registry<Structure> registry, ServerLevel level, BlockPos pos);
    }

    public record ByTag(TagKey<Structure> tag) implements StructureLocator {

        @Override
        @Nullable
        public Structure getStructure(Registry<Structure> registry, ServerLevel level, BlockPos pos) {
            return registry
                    .getTag(tag)
                    .map(h -> level.getChunkSource().getGenerator().findNearestMapStructure(level, h, pos, 1, false))
                    .map(Pair::getSecond)
                    .map(Holder::value)
                    .orElse(null);
        }
    }

    public record ById(ResourceKey<Structure> id) implements StructureLocator {

        @Override
        @Nullable
        public Structure getStructure(Registry<Structure> registry, ServerLevel level, BlockPos pos) {
            return registry.get(id);
        }
    }

    public static class Builder {
        private final List<ResourceKey<Structure>> ids = new ArrayList<>();
        private final List<TagKey<Structure>> tags = new ArrayList<>();

        public Builder add(String idOrTag) {
            if (idOrTag.startsWith("#")) {
                var rl = ResourceLocation.parse(idOrTag.substring(1));
                tags.add(TagKey.create(Registries.STRUCTURE, rl));
            } else {
                var rl = ResourceLocation.parse(idOrTag);
                ids.add(ResourceKey.create(Registries.STRUCTURE, rl));
            }
            return this;
        }

        public AnyStructure build(boolean exact) {
            var locators = new ArrayList<StructureLocator>();
            for (var id : ids) {
                locators.add(new ById(id));
            }
            for (var tag : tags) {
                locators.add(new ByTag(tag));
            }
            return new AnyStructure(locators, exact);
        }
    }
}
