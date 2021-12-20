package com.github.llytho.lootjs_test;

import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TestGlobalLootJSMod.MODID)
@Mod.EventBusSubscriber
public class TestGlobalLootJSMod {

    public static final String MODID = "lootjs_test";

    public TestGlobalLootJSMod() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    public static void command(CommandEvent event) {

    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {
        // MinecraftForge.EVENT_BUS.register(MyClassWithEvents.class);
    }
}
