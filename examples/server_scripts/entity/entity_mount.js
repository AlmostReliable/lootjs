onEvent("lootjs", (event) => {
    event
        .addLootTypeModifier([LootType.ENTITY])
        .matchEntity((entity) => {
            entity.anyType("#skeletons");
            entity.matchMount((mount) => {
                mount.anyType("minecraft:spider");
            });
        })
        .addLoot("minecraft:magma_cream");
});
