onEvent("lootjs", (event) => {
    event
        .addModifierForType([LootType.ENTITY])
        .matchEntity((entity) => {
            entity.anyType("#skeletons");
            entity.matchMount((mount) => {
                mount.anyType("minecraft:spider");
            });
        })
        .thenAdd("minecraft:magma_cream");
});
