/**
 * Don't mind this function. I use it for some testing. Just use `Item.of()` where you need it!
 */
function itemWithName(item, name) {
    return Item.of(item, `{display:{Name:'{\"text\":\"${name}\", \"color\":\"green\"}'}}`);
}

onEvent("lootjs", (event) => {
    event.enableLogging();
    event
        .addBlockLootModifier("minecraft:grass_block")
        .matchMainHand(ItemFilter.PICKAXE)
        .addLoot(itemWithName("paper", "Grass block with ItemFilter.PICKAXE"));
});

onEvent("lootjs", (event) => {
    event.enableLogging();
    event
        .addBlockLootModifier("minecraft:grass_block")
        .matchMainHand(ItemFilter.AXE)
        .addLoot(itemWithName("paper", "Grass block with ItemFilter.AXE"));
});

onEvent("lootjs", (event) => {
    event.enableLogging();
    event
        .addBlockLootModifier("minecraft:grass_block")
        .matchMainHand(ItemFilter.SWORD)
        .addLoot(itemWithName("paper", "Grass block with ItemFilter.SWORD"));
});

onEvent("lootjs", (event) => {
    event.enableLogging();
    event
        .addBlockLootModifier("minecraft:grass_block")
        .matchMainHand(ItemFilter.TOOL)
        .addLoot(itemWithName("paper", "Grass block with ItemFilter.TOOL"));
});

onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("minecraft:grass_block")
        .matchMainHand(ItemFilter.HAS_TIER)
        .addLoot(itemWithName("paper", "Grass block with ItemFilter.HAS_TIER"));
});

onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("minecraft:grass_block")
        .matchMainHand(ItemFilter.PROJECTILE_WEAPON)
        .addLoot(itemWithName("paper", "Grass block with ItemFilter.PROJECTILE_WEAPON"));
});

onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("minecraft:grass_block")
        .matchMainHand(ItemFilter.FOOD)
        .addLoot(itemWithName("paper", "Grass block with ItemFilter.FOOD"));
});

onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("minecraft:grass_block")
        .matchMainHand(ItemFilter.BLOCK)
        .addLoot(itemWithName("paper", "Grass block with ItemFilter.BLOCK"));
});
