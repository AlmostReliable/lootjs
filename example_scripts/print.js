LootJS.lootTables(event => {
    event.getLootTable("minecraft:chests/simple_dungeon").print()
})

LootJS.lootTables(event => {
    let pools = event.getLootTable("minecraft:chests/simple_dungeon").getPools();
    let hasItem = false;

    for (let pool of pools) {
        pool.modifyItemEntry((itemEntry) => {
            if (itemEntry.getItem() === "minecraft:gunpowder") {
                hasItem = true
            }

            return itemEntry; // We don't want to modify
        })

    }

    if (!hasItem) {
        throw new Error("Missing gunpowder in LootTable")
    }
})
