package com.almostreliable.lootjs;

import com.google.gson.Gson;
import net.minecraft.world.level.storage.loot.Deserializers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LootJS {
    public static final Logger LOG = LogManager.getLogger("LootJS");
    public static final Gson GSON = Deserializers.createConditionSerializer().create();
}
