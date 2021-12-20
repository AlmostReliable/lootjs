package com.github.llytho.lootjs.kube.builder;

import com.github.llytho.lootjs.util.TagOrEntry;
import com.github.llytho.lootjs.util.Utils;
import net.minecraft.advancements.criterion.BlockPredicate;
import net.minecraft.advancements.criterion.NBTPredicate;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.state.Property;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class BlockPredicateBuilderJS {

    private final StatePropertiesPredicate.Builder propBuilder = StatePropertiesPredicate.Builder.properties();
    private TagOrEntry<Block> tagOrEntry;

    private BlockPredicateBuilderJS() {}

    public static BlockPredicateBuilderJS block(String idOrTag) {
        BlockPredicateBuilderJS builder = new BlockPredicateBuilderJS();
        builder.tagOrEntry = Utils.getTagOrEntry(ForgeRegistries.BLOCKS, idOrTag);
        return builder;
    }

    public BlockPredicateBuilderJS properties(Map<String, String> propertyMap) {
        if (propertyMap.isEmpty()) return this;

        Block block = tagOrEntry.isTag() ? tagOrEntry.tag.getValues().get(0) : tagOrEntry.entry;
        Collection<Property<?>> properties = block.defaultBlockState().getProperties();
        for (Property<?> property : properties) {
            Object o = propertyMap.remove(property.getName());
            if (o != null) {
                Optional<?> value = property.getValue(o.toString());
                if (!value.isPresent()) {
                    throw new IllegalArgumentException(
                            "Property " + o + " does not exists for block " + block.getRegistryName());
                }
                propBuilder.hasProperty(property, value.get().toString());
            }
        }
        return this;
    }

    public BlockPredicate build() {
        return new BlockPredicate(tagOrEntry.tag, tagOrEntry.entry, propBuilder.build(), NBTPredicate.ANY);
    }
}
