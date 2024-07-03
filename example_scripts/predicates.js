function createNamedEntry(name) {
    return LootEntry.of(Item.of("minecraft:stick").withCustomName(name))
}

/**
 * Check location check automatic record building
 */
LootJS.lootTables((event) => {

    const entryWithLocCheck = createNamedEntry("LocationCheck").location({
        biomes: "#minecraft:is_overworld"
    })
    event
            .getEntityTable("minecraft:chicken")
            .createPool()
            .addEntry(entryWithLocCheck)
})

/**
 * Check entity predicate automatic record building
 */
LootJS.lootTables((event) => {

    const entryWithLocCheck = createNamedEntry("EntityPredicate").matchKiller({
        equipment: {
            mainhand: {
                items: "#c:tools"
            }
        },
        subPredicate: {
            type: "minecraft:lightning",
            blocks_set_on_fire: 0
        }
    })
    event
            .getEntityTable("minecraft:chicken")
            .createPool()
            .addEntry(entryWithLocCheck)
})


/**
 * Check item predicate automatic record building
 */
LootJS.lootTables((event) => {

    const ipRecordNested = createNamedEntry("ItemPredicate Record").matchTool({
        items: "#c:tools",
        // subPredicates: {
        //     "minecraft:damage": {
        //         durability: {
        //             min: 0,
        //             max: 10000
        //         },
        //         damage: {
        //             min: 0,
        //             max: 10000
        //         },
        //     }
        // }
    })
    // const ipRecord = createNamedEntry("ItemPredicate Record").matchTool({
    //     items: "#c:tools"
    // })
    // const ipFilter = createNamedEntry("ItemPredicate Record").matchTool(ItemFilter.tag("#c:tools"))
    // const ipAuto = createNamedEntry("ItemPredicate Record").matchTool("#c:tools")
    event
            .getBlockTable("minecraft:dirt")
            .createPool()
            .addEntry(ipRecordNested)
})

/**
 * Check item predicate automatic record building
 */
LootJS.lootTables((event) => {

    const ipRecordNested = createNamedEntry("ItemPredicate Record").testPlayerPredicate({
        level: {
            min: 0,
            max: 10000
        },
        gameType: ["survival", "creative"],
        stats: [],
        advancements: {
            "minecraft:test": true
        }
    })
    event
            .getBlockTable("minecraft:dirt")
            .createPool()
            .addEntry(ipRecordNested)
})
