package com.almostreliable.lootjs.util;

import com.almostreliable.lootjs.LootJS;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.registries.holdersets.HolderSetType;
import net.neoforged.neoforge.registries.holdersets.ICustomHolderSet;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class ModHolderSet<T> implements ICustomHolderSet<T> {
    public static <T> MapCodec<? extends ICustomHolderSet<T>> codec(ResourceKey<? extends Registry<T>> registryKey, Codec<Holder<T>> holderCodec, boolean forceList) {
        return RecordCodecBuilder.<ModHolderSet<T>>mapCodec(i -> i.group(
                RegistryOps.retrieveRegistryLookup(registryKey).forGetter(ModHolderSet::getRegistryLookup),
                Codec.STRING.fieldOf("mod").forGetter(ModHolderSet::getMod)
        ).apply(i, ModHolderSet::new));
    }

    private final HolderLookup.RegistryLookup<T> registryLookup;
    private final String mod;
    @Nullable
    private List<Holder<T>> elements;

    public ModHolderSet(HolderLookup.RegistryLookup<T> registryLookup, String mod) {
        this.mod = mod;
        this.registryLookup = registryLookup;
    }

    private List<Holder<T>> getElements() {
        if (this.elements == null) {
            this.elements = registryLookup
                    .listElements()
                    .filter(ref -> ref.key().location().getNamespace().equals(mod))
                    .map(ref -> (Holder<T>) ref)
                    .toList();
        }

        return this.elements;
    }

    @Override
    public Stream<Holder<T>> stream() {
        return this.registryLookup.listElements().map(Function.identity());
    }

    @Override
    public int size() {
        return (int) this.stream().count();
    }

    @Override
    public Either<TagKey<T>, List<Holder<T>>> unwrap() {
        return Either.right(getElements());
    }

    @Override
    public Optional<Holder<T>> getRandomElement(RandomSource random) {
        return Util.getRandomSafe(getElements(), random);
    }

    @Override
    public Holder<T> get(int i) {
        return getElements().get(i);
    }

    @Override
    public boolean contains(Holder<T> holder) {
        if (holder instanceof Holder.Reference<T> ref) {
            return ref.key().location().getNamespace().equals(mod);
        }

        return holder.unwrapKey().filter(key -> key.location().getNamespace().equals(mod)).isPresent();
    }

    @Override
    public boolean canSerializeIn(HolderOwner<T> holderOwner) {
        return registryLookup.canSerializeIn(holderOwner);
    }

    @Override
    public Optional<TagKey<T>> unwrapKey() {
        return Optional.empty();
    }

    @Override
    public HolderSetType type() {
        return LootJS.MOD_HOLDER_SET.value();
    }

    @NotNull
    @Override
    public Iterator<Holder<T>> iterator() {
        return getElements().iterator();
    }

    public HolderLookup.RegistryLookup<T> getRegistryLookup() {
        return registryLookup;
    }

    public String getMod() {
        return mod;
    }
}
