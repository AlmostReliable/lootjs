onEvent("lootjs", (event) => {
    event
        .addModifierForType("Spider jockey check", [LootType.ENTITY])
        .matchEntity((entity) => {
            entity.anyType("#skeletons");
            entity.matchMount((mount) => {
                mount.anyType("minecraft:spider");
            });
        })
        .thenAdd("minecraft:magma_cream");
});
