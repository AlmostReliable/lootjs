
onEvent("lootjs", (event) => {
    event.addBlockLootModifier("minecraft:diamond_block").playerAction((player) => {
        player.giveExperiencePoints(1000);
    });
});
