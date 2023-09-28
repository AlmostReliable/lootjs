package com.almostreliable.lootjs.fabric;

import com.almostreliable.lootjs.kube.LootJSEvent;
import com.almostreliable.lootjs.kube.LootTableEventJS;
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

        LootTableEvents.ALL_LOADED.register((resourceManager, lootManager) -> {
            LootTableEventJS event = new LootTableEventJS(lootManager);
            LootJSEvent.LOOT_TABLES.post(event);
        });
    }
}
