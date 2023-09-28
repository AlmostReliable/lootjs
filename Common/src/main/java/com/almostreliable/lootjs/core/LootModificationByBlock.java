package com.almostreliable.lootjs.core;

import com.google.common.base.Preconditions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;

public class LootModificationByBlock extends AbstractLootModification {
    private final Predicate<BlockState> predicate;

    public LootModificationByBlock(String name, Predicate<BlockState> predicate, List<ILootHandler> handlers) {
        super(name, handlers);
        Preconditions.checkNotNull(predicate);
        this.predicate = predicate;
    }

    @Override
    public boolean shouldExecute(LootContext context) {
        ILootContextData data = context.getParamOrNull(LootJSParamSets.DATA);
        BlockState blockState = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        return blockState != null && data != null && predicate.test(blockState) &&
               data.getLootContextType() == LootContextType.BLOCK;
    }
}
