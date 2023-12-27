package com.almostreliable.lootjs.loot.table;

import javax.annotation.Nullable;

public interface PostLootActionOwner {

    void lootjs$setPostLootAction(PostLootAction postLootAction);

    @Nullable
    PostLootAction lootjs$getPostLootAction();
}
