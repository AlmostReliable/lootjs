LootJS.modifiers(event => {
    const entry = LootEntry.testItem("ItemFilter Tag Negate").matchMainHand("!#c:tools")
    event.addEntityModifier("minecraft:cow").addLoot(entry)
})
