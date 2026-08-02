package com.almostreliable.lootjs;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;
import java.util.stream.Stream;

public class LookupProvider implements HolderLookup.Provider {

    private final RegistryAccess registryAccess;

    public LookupProvider(RegistryAccess registryAccess) {
        this.registryAccess = registryAccess;
    }

    @Override
    public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
        return registryAccess.listRegistryKeys();
    }

    @Override
    public <T> Optional<Registry<T>> lookup(ResourceKey<? extends Registry<? extends T>> arg) {
        return registryAccess.lookup(arg);
    }
}
