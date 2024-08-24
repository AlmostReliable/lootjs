LootJS.lootTables((event) => {
    event.getBlockTable('minecraft:coal_ore')
            .clear()
            .firstPool()
            .addEntry(LootEntry.of('minecraft:diamond').matchCustomCondition({
                condition: "minecraft:match_tool",
                predicate: {
                    enchantments: [
                        {
                            enchantment: "minecraft:silk_touch",
                            levels: {
                                min: 1,
                            },
                        },
                    ],
                },
            }));
})
