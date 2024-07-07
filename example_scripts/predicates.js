/**
 * Check location check automatic record building
 */
LootJS.modifiers(event => {
    const entry = LootEntry.testItem("LocationCheck").matchLocation({
        position: {
            x: 30,
            y: {
                min: 0,
                max: 200
            },
            z: 30
        },
        biomes: "#minecraft:is_overworld",
        dimension: "minecraft:overworld",
        smokey: false,
        light: {
            min: 0,
            max: 15
        },
        block: {
            blocks: "#minecraft:mineable/pickaxe",
            properties: {
                waterlogged: false
            }
        },
        canSeeSky: true
    })

    event.addEntityModifier("minecraft:chicken").addLoot(entry)
})

/**
 * Check entity check automatic record building
 */
LootJS.modifiers(event => {
    const entry = LootEntry.testItem("EntityPredicate").matchEntity({
        entityType: "@minecraft",
        distance: {
            absolute: {
                min: 0,
                max: 100
            }
        },
        movement: {
            speed: {
                min: 0,
                max: 100
            },
            fallDistance: {
                min: 0,
                max: 100
            }
        },
        location: {
            located: {
                // LocationPredicate
            },
            steppingOn: {
                // LocationPredicate
            }
        },
        effects: [
            {
                id: "minecraft:strength",
                duration: {
                    max: 400
                }
            }
        ],
        flags: {
            isOnGround: true
        },
        equipment: {
            // ItemPredicate for each equipment slot
        },
        vehicle: {
            // EntityPredicate
        },
        passenger: {
            // EntityPredicate
        }
    })

    event.addEntityModifier("minecraft:chicken").addLoot(entry)
})

/**
 * Damage source test
 */
LootJS.modifiers(event => {
    const entry = LootEntry.testItem("DamageSource").matchDamageSource({
        tags: [
            {
                expected: true,
                id: "minecraft:is_explosion"
            },
        ]
    })

    event.addEntityModifier("minecraft:chicken").addLoot(entry)
})

/**
 * Item predicate test
 */
LootJS.modifiers(event => {
    const entry = LootEntry.testItem("ItemPredicate").matchTool({
        items: {
            type: "neoforge:or",
            values: [
                "@minecraft",
                "#c:tools",
                /someRegexValue/,
                {
                    type: "neoforge:any"
                }
            ]
        },
        predicates: {
            enchantments: [
                {
                    enchantments: "@minecraft", // Checks if nested holder set still works in predicate
                    levels: {
                        min: 3
                    }
                }
            ]
        }
    })

    event.addEntityModifier("minecraft:chicken").addLoot(entry)
})

/**
 * Distance predicate test
 */
LootJS.modifiers(event => {
    const entry = LootEntry.testItem("DistancePredicate").matchDistance({
        absolute: {
            min: 42,
        }
    })

    event.addEntityModifier("minecraft:chicken").addLoot(entry)
})
