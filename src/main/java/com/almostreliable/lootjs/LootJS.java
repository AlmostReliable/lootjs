package com.almostreliable.lootjs;

import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

@Mod("lootjs")
public class LootJS {
    public static final Logger LOG = LogManager.getLogger("LootJS");
    public static Consumer<String> DEBUG_ACTION = LOG::info;

    public LootJS() {

    }
}
