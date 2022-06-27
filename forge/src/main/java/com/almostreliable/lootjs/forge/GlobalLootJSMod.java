package com.almostreliable.lootjs.forge;

import com.almostreliable.lootjs.core.Constants;
import com.google.gson.Gson;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MODID)
public class GlobalLootJSMod {

    public static Gson GSON = Deserializers.createConditionSerializer().create();

    public GlobalLootJSMod() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {

        // MinecraftForge.EVENT_BUS.register(MyClassWithEvents.class);
    }
}
