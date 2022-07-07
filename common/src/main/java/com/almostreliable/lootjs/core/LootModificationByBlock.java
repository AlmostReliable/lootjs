package com.almostreliable.lootjs.core;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.HashSet;
import java.util.List;

public class LootModificationByBlock extends AbstractLootModification {

    public final HashSet<Block> blocks;

    public LootModificationByBlock(String name, HashSet<Block> blocks, List<ILootHandler> handlers) {
        super(name, handlers);
        this.blocks = blocks;
    }

    @Override
    public boolean shouldExecute(LootContext context) {
        ILootContextData data = context.getParamOrNull(LootJSParamSets.DATA);
        BlockState blockState = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        return blockState != null && data != null && blocks.contains(blockState.getBlock()) &&
               data.getLootContextType() == LootContextType.BLOCK;
    }
}
