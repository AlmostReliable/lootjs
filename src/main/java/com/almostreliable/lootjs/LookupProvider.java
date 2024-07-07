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
    public Stream<ResourceKey<? extends Registry<?>>> listRegistries() {
        return registryAccess.listRegistries();
    }

    @Override
    public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> arg) {
        return registryAccess.registry(arg).map(Registry::asTagAddingLookup);
    }
}
