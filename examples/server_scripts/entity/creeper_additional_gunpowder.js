onEvent("lootjs", (event) => {
    event
        .addModifierForLootTable("minecraft:entities/creeper")
        .randomChance(0.3) // 30% chance
        .thenAdd("minecraft:gunpowder");
});
