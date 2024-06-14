package com.almostreliable.lootjs;

import com.almostreliable.lootjs.core.filters.ItemFilterWrapper;
import com.almostreliable.lootjs.util.ModHolderSet;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.holdersets.HolderSetType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

@Mod("lootjs")
public class LootJS {
    public static final Logger LOG = LogManager.getLogger("LootJS");
    public static Consumer<String> DEBUG_ACTION = LOG::info;
    private static final DeferredRegister<HolderSetType> HOLDER_SET_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.HOLDER_SET_TYPES,
            BuildConfig.MOD_ID);
    public static final Holder<HolderSetType> MOD_HOLDER_SET = HOLDER_SET_TYPES.register("by_mod",
            () -> ModHolderSet::codec);

    public LootJS(IEventBus bus) {
        bus.addListener(this::onRegister);
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
