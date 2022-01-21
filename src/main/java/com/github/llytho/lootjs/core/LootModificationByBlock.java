package com.github.llytho.lootjs.core;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;

import java.util.HashSet;
import java.util.List;

public class LootModificationByBlock extends AbstractLootModification {

    public final HashSet<Block> blocks;

    public LootModificationByBlock(String name, HashSet<Block> blocks, List<ILootAction> actions) {
        super(name, actions);
        this.blocks = blocks;
    }

    @Override
    public boolean shouldExecute(LootContext context) {
        BlockState blockState = context.getParamOrNull(LootParameters.BLOCK_STATE);
        return blockState != null && blocks.contains(blockState.getBlock());
    }
}
