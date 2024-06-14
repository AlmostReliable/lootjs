package com.almostreliable.lootjs;

import com.almostreliable.lootjs.core.filters.ItemFilterWrapper;
import com.almostreliable.lootjs.util.ModHolderSet;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.holdersets.HolderSetType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Mod("lootjs")
public class LootJS {
    public static final Logger LOG = LogManager.getLogger("LootJS");
    public static Consumer<String> DEBUG_ACTION = LOG::info;
    private static final DeferredRegister<HolderSetType> HOLDER_SET_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.HOLDER_SET_TYPES,
            BuildConfig.MOD_ID);
    public static final Holder<HolderSetType> MOD_HOLDER_SET = HOLDER_SET_TYPES.register("by_mod",
            () -> ModHolderSet::codec);
    public static ThreadLocal<HolderLookup.Provider> LOOKUP_PROVIDER = ThreadLocal.withInitial(() -> new HolderLookup.Provider() {
        @Override
        public Stream<ResourceKey<? extends Registry<?>>> listRegistries() {
            return Stream.empty();
        }

        @Override
        public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> arg) {
            return Optional.empty();
        }
    });

    public LootJS(IEventBus bus) {
        bus.addListener(this::onRegister);
    }

    public static HolderLookup.Provider lookup() {
        return LOOKUP_PROVIDER.get();
    }

    public static void storeLookup(HolderLookup.Provider lookup) {
        LOOKUP_PROVIDER.set(lookup);
    }

    /**
     * We mainly register the item sub predicate so in case some serialization happens, nothing breaks. But we don't
     * really serialize our ItemFilter's as they are dynamic and can't be serialized.
     */
    private void onRegister(RegisterEvent event) {
        if (event.getRegistry() == BuiltInRegistries.ITEM_SUB_PREDICATE_TYPE) {
            event.register(Registries.ITEM_SUB_PREDICATE_TYPE,
                    ResourceLocation.fromNamespaceAndPath(BuildConfig.MOD_ID, "item"),
                    () -> ItemFilterWrapper.TYPE);
        }
    }
}
