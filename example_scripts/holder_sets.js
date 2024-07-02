LootJS.modifiers(event => {
    event.addEntityModifier(["minecraft:cow", "minecraft:sheep"]).addLoot("minecraft:acacia_door")
})

LootJS.modifiers(event => {
    event.addEntityModifier("minecraft:chicken").addLoot("minecraft:dirt")
})

LootJS.modifiers(event => {
    event.addEntityModifier("#minecraft:undead").addLoot("minecraft:gravel")
})

LootJS.modifiers(event => {
    event.addEntityModifier("@minecraft").addLoot("minecraft:stone_stairs")
})

LootJS.modifiers(event => {
    event.addEntityModifier(/.*spider.*/).addLoot("minecraft:diamond")
})
