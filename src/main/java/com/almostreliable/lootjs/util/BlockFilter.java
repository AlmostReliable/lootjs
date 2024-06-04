package com.almostreliable.lootjs.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface BlockFilter extends Iterable<Block>, Predicate<BlockState> {

    default Stream<Block> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
