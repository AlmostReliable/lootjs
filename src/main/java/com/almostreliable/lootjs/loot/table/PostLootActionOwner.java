package com.almostreliable.lootjs.loot.table;

import org.jspecify.annotations.Nullable;

public interface PostLootActionOwner {

    void lootjs$setPostLootAction(PostLootAction postLootAction);

    @Nullable
    PostLootAction lootjs$getPostLootAction();
}
