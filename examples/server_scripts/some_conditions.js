/**
 * Don't mind this function. I use it for some testing. Just use `Item.of()` where you need it!
 */
function itemWithName(item, name) {
    return Item.of(item, `{display:{Name:'{\"text\":\"${name}\", \"color\":\"green\"}'}}`);
}

onEvent("lootjs", (event) => {
    event
        .addEntityLootModifier("minecraft:zombie")
        .anyStructure("minecraft:stronghold", false)
        .addLoot(itemWithName("paper", "Zombie killed in stronghold"));
});

onEvent("lootjs", (event) => {
    event
        .addEntityLootModifier("minecraft:zombie")
        .anyBiome("#minecraft:is_forest")
        .addLoot(itemWithName("paper", "Zombie killed in forest"));
});

onEvent("lootjs", (event) => {
    event
        .addEntityLootModifier("minecraft:zombie")
        .anyBiome("desert")
        .addLoot(itemWithName("paper", "Zombie killed in destert"));
});

