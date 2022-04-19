/**
 * Don't mind this function. I use it for some testing. Just use `Item.of()` where you need it!
 */
function itemWithName(item, name) {
    return Item.of(item, `{display:{Name:'{\"text\":\"${name}\", \"color\":\"green\"}'}}`);
}

onEvent("lootjs", (event) => {
    event.addEntityLootModifier("minecraft:chicken").functions([Item.of("chicken"), ItemFilter.FOOD], (f) => {
        f.smeltLoot();
    });
});

onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("minecraft:emerald_block")
        .addLoot(itemWithName("iron_sword", "enchantRandom() triggered"))
        .enchantRandomly();
});

onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("minecraft:emerald_block")
        .addLoot(itemWithName("iron_sword", "enchantWithLevels() triggered"))
        .enchantWithLevels([2, 3]);
});

onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("minecraft:emerald_block")
        .addLoot(itemWithName("coal_ore", "applyLootingBonus() triggered"))
        .applyLootingBonus([3, 10]);
});

onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("minecraft:emerald_block")
        .addLoot(itemWithName("iron_sword", "damage() triggered"))
        .damage([0.01, 0.2]);
});

onEvent("lootjs", (event) => {
    event.addBlockLootModifier("minecraft:emerald_block").pool((p) => {
        p.addLoot(itemWithName("minecraft:potion", "addPotion() triggered"));
        p.addPotion("poison");
    });
});

onEvent("lootjs", (event) => {
    event.addBlockLootModifier("minecraft:emerald_block").pool((p) => {
        p.addLoot(itemWithName("minecraft:nether_star", "addLore() triggered"));
        p.addLore(["This is a lore", Text.red("This is a red lore")]);
    });
});

onEvent("lootjs", (event) => {
    event.addBlockLootModifier("minecraft:emerald_block").pool((p) => {
        p.addLoot("minecraft:apple");
        p.setName(Text.blue("This is a blue name"));
    });
});

onEvent("lootjs", (event) => {
    event.addBlockLootModifier("minecraft:emerald_block").pool((p) => {
        p.addLoot(Item.of("minecraft:gunpowder", 30)); // should be reduced to 15
        p.addLoot(Item.of("minecraft:blaze_powder", 1)); // should be bumped up to at least 5
        p.limitCount([5, 10], [15]);
    });
});
