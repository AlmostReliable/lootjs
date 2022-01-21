onEvent("lootjs", (event) => {
    // all leaves disabled per regex
    event.disableLootModification(/.*:blocks\/.*_leaves/);
    
    // disable bats
    event.disableLootModification("minecraft:entities/bat");
});
