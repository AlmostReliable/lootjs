LootJS.modifiers(event => {
    event.addTableModifier(LootType.CHEST, /minecraft:entities.*/).customAction(() => {
        console.log("Triggerd LootJS.modifiers for Chests and all entities");
    })
});

LootJS.lootTables(event => {
    console.log("All Chest and Entity loot tables: ")
    for (let lt of event.modifyLootTables(LootType.CHEST, /minecraft:entities.*/)) {
        console.log(" - " + lt.location)
    }
})
