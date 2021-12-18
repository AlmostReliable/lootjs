package com.github.llytho.lootjs.core;

public interface ILootModificationResult {

    void pushLayer();

    void popLayer();

    void add(boolean succeed, Object o);

    void finish();
}
