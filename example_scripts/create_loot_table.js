LootJS.lootTables(e => {
    e.create("lootjs:blocks/test_table", "block").createPool((pool) => {
        pool.addEntry(LootEntry.of("minecraft:stick").limitCount(10, 32));
    });
});
