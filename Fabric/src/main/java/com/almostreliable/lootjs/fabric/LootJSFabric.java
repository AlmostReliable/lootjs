package com.almostreliable.lootjs.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;

public class LootJSFabric implements ModInitializer {


    @Override
    public void onInitialize() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if(tableBuilder instanceof LootTableIdOwner lte) {
                lte.lootjs$setLootTableId(id);
            }
        });
    }
}
