package com.github.llytho.lootjs;

import com.github.llytho.lootjs.core.Constants;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Constants.MODID)
public class GlobalLootJSMod {

    public static final Logger LOGGER = LogManager.getLogger();

    public GlobalLootJSMod() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {
        // MinecraftForge.EVENT_BUS.register(MyClassWithEvents.class);
    }
}
