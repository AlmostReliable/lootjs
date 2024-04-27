package com.almostreliable.lootjs.util;

import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface BlockFilter extends Iterable<Block> {

    default Stream<Block> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
