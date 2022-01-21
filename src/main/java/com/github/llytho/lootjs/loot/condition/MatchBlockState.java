package com.github.llytho.lootjs.loot.condition;

import com.github.llytho.lootjs.util.TagOrEntry;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;

public class MatchBlockState implements IExtendedLootCondition {

    private final TagOrEntry<Block> tagOrEntry;
    private final StatePropertiesPredicate properties;

    public MatchBlockState(TagOrEntry<Block> tagOrEntry, StatePropertiesPredicate properties) {
        this.tagOrEntry = tagOrEntry;
        this.properties = properties;
    }

    @Override
    public boolean test(LootContext context) {
        BlockState blockState = context.getParamOrNull(LootParameters.BLOCK_STATE);
        return blockState != null && tagOrEntry.is(blockState.getBlock()) && properties.matches(blockState);
    }
}
