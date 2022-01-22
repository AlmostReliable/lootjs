onEvent("lootjs", (event) => {
    event
        .addEntityLootModifier("minecraft:creeper")
        .randomChance(0.3) // 30% chance
        .thenAdd("minecraft:gunpowder");
});
